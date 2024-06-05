public enum Priorities {
    LOWEST(2),
    LOW(4),
    MEDIUM(6),
    HIGH(8),
    CRITICAL(10),
    NO_PRIORITY(0);
    private final int priorityValue;

    Priorities(int priorityValue){
        this.priorityValue = priorityValue;
    }

    public int getPriorityValue(){
        return this.priorityValue;
    }
}
