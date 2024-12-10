public final class Hospital {
    // The internal instance
    private static Hospital instance;

    private Hospital() {
    }

    public static Hospital getInstance() {
        if (instance == null) {
            instance = new Hospital();
        }
        return instance;
    }
}
