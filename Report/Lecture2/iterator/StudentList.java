public class StudentList {
    protected Student[] students;
    private int last = 0;

    public StudentList() {
    }

    public StudentList(int studentCount) {
        this.students = new Student[studentCount];
    }

    public void add(Student student) {
        students[last] = student;
        last++;
    }

    public Student getStudentAt(int index) {
        return students[index];
    }

    public int getLastNum() {
        return last;
    }
}
