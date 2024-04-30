package at.codersbay.household.model;

public class Pet {

    private int id;
    private String type;
    private int quantity;
    private Person person;

    public Pet(int id, String type, int quantity) {
        setId(id);
        setType(type);
        setQuantity(quantity);
    }

    public Pet(String type, int quantity, Person person) {
        setType(type);
        setQuantity(quantity);
        setPerson(person);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty.");
        }
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "ID = " + id + ", Type = " + type + ", Quantity = " + quantity;
    }
}
