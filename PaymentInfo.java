public enum PaymentInfo {

    // Status
    PAID,
    PENDING,
    OVERDUE,

    // Method
    POS_ON_DELIVERY,
    BANK_TRANSFER;

    // Check if value is a status
    public boolean isStatus() {
        return this == PAID || this == PENDING || this == OVERDUE;
    }

    // Check if value is a method
    public boolean isMethod() {
        return this == POS_ON_DELIVERY || this == BANK_TRANSFER;
    }
}
