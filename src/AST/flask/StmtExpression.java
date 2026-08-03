
package AST.flask;

public class StmtExpression implements Statement {
    private Expression expression;

    public StmtExpression(Expression var1) {
        this.expression = var1;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public String toString() {
        return "StmtExpression{expression=" + String.valueOf(this.expression) + "}";
    }
}
