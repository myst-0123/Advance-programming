package Report.Lecture2.singleton;

public class RegisterNote {
    private static RegisterNote registerNote = new RegisterNote();

    private RegisterNote() {
    }

    public static RegisterNote getInstance() {
        return registerNote;
    }
}