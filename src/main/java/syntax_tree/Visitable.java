package syntax_tree;

public interface Visitable {
    void accept(Visitor visitor);
}
