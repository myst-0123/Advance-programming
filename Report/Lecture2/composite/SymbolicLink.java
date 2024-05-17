package Report.Lecture2.composite;

public class SymbolicLink implements DirectoryEntry {
    private String name = null;

    public SymbolicLink(String name) {
        this.name = name;
    }

    public void remove() {
        System.out.println(name + "を削除しました");
    }
}
