package MainApp;

import AST.flask.Program;
import antler.FlaskLexer;
import antler.FlaskParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import generator.Generator;
import serializer.FlaskAstJsonSerializer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import Visitor.Visitor;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static Utils.assetsMethode.removeEmpty;

public class Main {
    public static List<Symantic_Error> errors = new ArrayList<>();
    public static check_Symantic_Error semanticError = new check_Symantic_Error();

    public static boolean first = true;
  public static int id_element=0;
    public static void main(String[] args) throws IOException {


        String filePath = args.length > 0
                ? args[0]
                : Paths.get("project", "app.py").toAbsolutePath().toString();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("الملف غير موجود!");
            return;
        }


        FlaskLexer lexer = new FlaskLexer(CharStreams.fromFileName(filePath));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FlaskParser parser = new FlaskParser(tokens);


        ParseTree tree = parser.program();
        Visitor visitor = new Visitor();
        Program programAst = (Program) visitor.visit(tree);



        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        FlaskAstJsonSerializer serializer = new FlaskAstJsonSerializer();
        JsonElement root = serializer.serialize(programAst);

        removeEmpty(root);

        String json = gson.toJson(root);

        Files.createDirectories(Paths.get("compiler_output"));

        Files.writeString(
                Paths.get("compiler_output", "ast_python.json"),
                json,
                StandardCharsets.UTF_8
        );
        semanticError.check_Errors();

        System.out.println("========== AST ==========");
        System.out.print(programAst);
      Generator generator =
        new Generator();

        generator.generate(programAst);
        semanticError = new check_Symantic_Error() ;
        errors = new ArrayList<>();
    }

}
