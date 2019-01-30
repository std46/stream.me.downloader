package stream.me;

public enum Status {
    WAITING("Waiting..."),
    DOWNLOADING_META("Downloading Meta Data..."),
    DOWNLOADING_VIDEO("Downloading Video..."),
    FINISHED("Finished"),
    FAILURE("Download Failed");

    private final String str;

    Status(String str) {
        this.str = str;
    }


    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return str;
    }
}
