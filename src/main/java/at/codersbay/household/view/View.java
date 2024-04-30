package at.codersbay.household.view;

import at.codersbay.household.dal.*;
import at.codersbay.household.model.Gender;
import at.codersbay.household.model.Household;
import at.codersbay.household.model.Person;
import at.codersbay.household.model.Pet;

import javax.naming.InvalidNameException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class View {
    private final Scanner sc;
    private final HhDao hhDao;
    private final PersonDao personDao;
    private final PetDao petDao;
    private final InitializationDao iniDao;
    int userChoice = -1;


    public View() {
        this.sc = new Scanner(System.in);
        this.hhDao = new HhDaoMysql();
        this.personDao = new PersonDaoMysql();
        this.petDao = new PetDaoMysql();
        this.iniDao = new InitializationDaoMysql();
    }


    public Household createHousehold() throws IOException {
        String streetName, city;
        int streetNr, plz;

        while (true) {
            System.out.print("Enter street-name: ");
            streetName = sc.nextLine();
            if (containsDigit(streetName)) {
                System.out.println("Street name contains a digit, which is invalid.");
                continue;
            }
            break;
        }

        while (true) {
            try {
                System.out.print("Enter street-nr: ");
                streetNr = sc.nextInt();
                sc.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid street number. Please enter a numeric value.");
                sc.nextLine();
            }
        }

        while (true) {
            try {
                System.out.print("Enter plz: ");
                plz = sc.nextInt();
                sc.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid PLZ. Please enter a numeric value.");
                sc.nextLine();
            }
        }

        while (true) {
            System.out.print("Enter city: ");
            city = sc.nextLine();
            if (containsDigit(city)) {
                System.out.println("City name contains a digit, which is invalid.");
                continue;
            }
            break;
        }

        return new Household(streetName, streetNr, plz, city);
    }

    public boolean containsDigit(String s) {
        return s.matches(".*\\d.*");
    }

    public Person createPerson() {
        String lastname;
        String firstname;
        while (true) {
            try {
                System.out.print("Enter firstname: ");
                firstname = sc.nextLine();
                if (containsDigit(firstname)) {
                    throw new InvalidNameException("Firstname contains a digit, which is invalid.");
                }

                System.out.print("Enter lastname: ");
                lastname = sc.nextLine();
                if (containsDigit(lastname)) {
                    throw new InvalidNameException("Lastname contains a digit, which is invalid.");
                }

                break;
            } catch (InvalidNameException e) {
                System.out.println(e.getMessage() + " Please try again.");
            }
        }
        Gender gender = selectGender();
        LocalDate birthdate = createBirthdate(sc);
        int householdId;
        while (true) {
            try {
                System.out.println("Enter HouseholdID: ");
                householdId = sc.nextInt();
                sc.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid HouseholdID. Please enter a numeric value.");
                sc.nextLine();
            }
        }
        return new Person(firstname, lastname, birthdate, gender, householdId);
    }

    public Gender selectGender() {
        while (true) {
            System.out.print("Select gender (1 - Male, 2 - Female): ");
            int choice;
            while (true) {
                try {
                    choice = sc.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid HouseholdID. Please enter a numeric value.");
                    sc.nextLine();
                }
            }

            switch (choice) {
                case 1 -> {
                    return Gender.male;
                }
                case 2 -> {
                    return Gender.female;
                }
                default -> System.out.println("Invalid selection. Please enter 1 for Male or 2 for Female.");

            }

        }

    }

    public LocalDate createBirthdate(Scanner sc) {
        int day, month, year;

        // Schleife für den Tag
        while (true) {
            try {
                System.out.print("Enter birthday (DD): ");
                day = sc.nextInt();
                sc.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid birthday. Please enter a numeric value.");
                sc.nextLine();
            }
        }

        // Schleife für den Monat
        while (true) {
            try {
                System.out.print("Enter birth-month (MM): ");
                month = sc.nextInt();
                sc.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid birth-month. Please enter a numeric value.");
                sc.nextLine();
            }
        }

        // Schleife für das Jahr
        while (true) {
            try {
                System.out.print("Enter birth-year (YYYY): ");
                year = sc.nextInt();
                sc.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid birth-year. Please enter a numeric value.");
                sc.nextLine();
            }
        }

        return LocalDate.of(year, month, day);
    }

    public Pet createPet() {
        String type;
        int quantity;
        Person person;
        while (true) {
            try {
                System.out.print("Enter type: ");
                type = sc.nextLine();
                if (containsDigit(type)) {
                    throw new InvalidNameException("Type contains a digit, which is invalid.");
                }

                System.out.print("Enter quantity: ");
                quantity = sc.nextInt();
                sc.nextLine();

                person = checkPersonExist();
                if (person == null) {
                    System.out.println("No matching person found. Please try again.");
                    continue;
                }

                break;
            } catch (InvalidNameException e) {
                System.out.println(e.getMessage() + " Please try again.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }

        return new Pet(type, quantity, person);
    }

    public Person checkPersonExist() {
        String firstname;
        String lastname;

        while (true) {
            try {
                System.out.print("Enter firstname: ");
                firstname = sc.nextLine();
                if (containsDigit(firstname)) {
                    throw new InvalidNameException("Firstname contains a digit, which is invalid.");
                }

                System.out.print("Enter lastname: ");
                lastname = sc.nextLine();
                if (containsDigit(lastname)) {
                    throw new InvalidNameException("Lastname contains a digit, which is invalid.");
                }

                return personDao.checkPersonExist(firstname, lastname);

            } catch (InvalidNameException e) {
                System.out.println(e.getMessage() + " Please try again.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    public void displayWelcomeMenu() throws IOException {
        iniDao.createTables();
        int userChoice = 0;

        do {
            System.out.println("What do you want to do?");
            System.out.println("0. Show all-menu");
            System.out.println("1. Create-menu");
            System.out.println("2. Read-menu");
            System.out.println("3. Update-menu");
            System.out.println("4. Delete-menu");
            System.out.println("5. Exit");

            try {
                userChoice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }

            switch (userChoice) {
                case 0 -> displayAllMenu();
                case 1 -> displayCreateMenu();
                case 2 -> displayReadMenu();
                case 3 -> displayUpdateMenu();
                case 4 -> displayDeleteMenu();
                case 5 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice. Please enter a number between 0 and 5.");
            }

        } while (userChoice != 5);
    }

    private void displayAllMenu() throws IOException {
        String[] deleteMenuItems = {"Show all household", "Show all person", "Show all pet", "<- go back"};
        int[] deleteMenuNr = {0, 1, 2, 3};

        do {

            System.out.println("What you wanna do?");
            for (int i = 0; i < deleteMenuNr.length; i++) {
                if (deleteMenuNr[i] == 3) System.out.println("7. " + deleteMenuItems[i]);
                else System.out.println(deleteMenuNr[i] + ". " + deleteMenuItems[i]);
            }

            try {
                userChoice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Wrong Datatype");
                sc.next();
            }

            //Show all household
            if (userChoice == 0) {
                List<Household> hh = hhDao.getAllHouseholds();
                for (Household household : hh) {
                    System.out.println(household);
                }
            }

            //Show all person
            if (userChoice == 1) {
                List<Person> persons = personDao.getAllPersons();
                for (Person person : persons) {
                    System.out.println(person);
                }
            }

            //Show all pet
            if (userChoice == 2) {
                List<Pet> pets = petDao.getAllPets();
                for (Pet pet : pets) {
                    System.out.println(pet);
                }
            }

        } while (userChoice != 7);
    }

    private void displayCreateMenu() throws IOException {
        String[] readMenuItems = {"Create a household", "Create a person", "Create a pet", "<- go back"};
        int[] readMenuNr = {0, 1, 2, 3};

        do {

            System.out.println("What you wanna do?");
            for (int i = 0; i < readMenuNr.length; i++) {
                if (readMenuNr[i] == 3) System.out.println("7. " + readMenuItems[i]);
                else System.out.println(readMenuNr[i] + ". " + readMenuItems[i]);
            }

            try {
                userChoice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Wrong Datatype");
                sc.next();
            }

            //create a household
            if (userChoice == 0) {
                Household hh = createHousehold();
                hh = hhDao.createHousehold(hh);
                System.out.println(hh);
            }

            //create a person
            if (userChoice == 1) {
                Person p = createPerson();
                p = personDao.createPerson(p);
                System.out.println(p);
            }

            //ceate a pet
            if (userChoice == 2) {
                Pet pet = createPet();
                pet = petDao.createPet(pet);
                System.out.println(pet);
            }


        } while (userChoice != 7);
    }

    private void displayReadMenu() throws IOException {
        String[] readMenuItems = {"Read a household", "Read a person", "Read a pet", "<- go back"};
        int[] readMenuNr = {0, 1, 2, 3};

        do {

            System.out.println("What you wanna do?");
            for (int i = 0; i < readMenuNr.length; i++) {
                if (readMenuNr[i] == 3) System.out.println("7. " + readMenuItems[i]);
                else System.out.println(readMenuNr[i] + ". " + readMenuItems[i]);
            }

            try {
                userChoice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Wrong Datatype");
                sc.next();
            }

            //read a household
            if (userChoice == 0) {
                System.out.println("Please enter the ID: ");
                int id = sc.nextInt();
                hhDao.readHousehold(id);
            }

            //read a person
            if (userChoice == 1) {
                System.out.println("Please enter the ID: ");
                int id = sc.nextInt();
                personDao.readPerson(id);
            }

            //read a pet
            if (userChoice == 2) {
                System.out.println("Please enter the ID: ");
                int id = sc.nextInt();
                petDao.readPet(id);
            }


        } while (userChoice != 7);
    }


    private void displayUpdateMenu() {
        String[] updateMenuItems = {"Update a household", "Update a person", "Update a pet", "<- go back"};
        int[] updateMenuNr = {0, 1, 2, 3};

        do {

            System.out.println("What you wanna do?");
            for (int i = 0; i < updateMenuNr.length; i++) {
                if (updateMenuNr[i] == 3) System.out.println("7. " + updateMenuItems[i]);
                else System.out.println(updateMenuNr[i] + ". " + updateMenuItems[i]);
            }

            try {
                userChoice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Wrong Datatype");
                sc.next();
            }

            //update household
            if (userChoice == 0) {
                try {
                    System.out.print("Enter the ID of the household to be updated: ");
                    int householdId = Integer.parseInt(sc.nextLine());

                    System.out.print("Enter the new streetname: ");
                    String newStreetName = sc.nextLine();

                    System.out.print("Enter the new streetNr: ");
                    int newStreetNr = Integer.parseInt(sc.nextLine());

                    System.out.print("Enter the new plz: ");
                    int newPLZ = Integer.parseInt(sc.nextLine());

                    System.out.print("Enter the new city: ");
                    String newCity = sc.nextLine();


                    hhDao.updateHousehold(householdId, newStreetName, newStreetNr, newPLZ, newCity);

                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    System.err.println("An error occurred while updating the person: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("An unexpected error occurred: " + e.getMessage());

                }
            }

            //update person
            if (userChoice == 1) {

                try {
                    System.out.print("Enter the ID of the person to be updated: ");
                    int personId = Integer.parseInt(sc.nextLine());

                    System.out.print("Enter the new firstname: ");
                    String newFirstname = sc.nextLine();

                    System.out.print("Enter the new lastname: ");
                    String newLastname = sc.nextLine();

                    System.out.print("Enter the new date of birth (format YYYY-MM-DD): ");
                    String dateString = sc.nextLine();
                    LocalDate newBirthdate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    System.out.print("Enter the new gender (1 for Male, 2 for Female): ");
                    int genderChoice = sc.nextInt();
                    sc.nextLine(); // Consume the newline

                    Gender newGender;
                    if (genderChoice == 1) {
                        newGender = Gender.male;
                    } else if (genderChoice == 2) {
                        newGender = Gender.female;
                    } else {
                        throw new IllegalArgumentException("Invalid gender choice. Please enter 1 for Male or 2 for Female.");
                    }
                    Person person = new Person(personId, newFirstname,
                            newLastname, newBirthdate, newGender);
                    try {
                        personDao.updatePerson(person);
                        System.out.println("org.nermin.at.codersbay.household.model.Person's data successfully updated.");
                    } catch (IOException e) {
                        System.err.println("An error occurred while updating the person: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("An unexpected error occurred: " + e.getMessage());

                    }
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }
            }

            //update pet
            if (userChoice == 2) {
                try {
                    System.out.print("Enter the ID of the pet to be updated: ");
                    int petId = Integer.parseInt(sc.nextLine());

                    System.out.print("Enter the new type: ");
                    String newType = sc.nextLine();

                    System.out.print("Enter the new quantity: ");
                    int newQuantity = sc.nextInt();
                    sc.nextLine();


                    try {
                        petDao.updatePet(petId, newType, newQuantity);
                    } catch (IOException e) {
                        System.err.println("An error occurred while updating the pet: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("An unexpected error occurred: " + e.getMessage());

                    }
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }
            }

        } while (userChoice != 7);
    }

    public void displayDeleteMenu() {
        String[] deleteMenuItems = {"Delete a household", "Delete a person", "Delete a pet", "<- go back"};
        int[] deleteMenuNr = {0, 1, 2, 3};

        do {


            System.out.println("What you wanna do?");
            for (int i = 0; i < deleteMenuNr.length; i++) {
                if (deleteMenuNr[i] == 3) System.out.println("7. " + deleteMenuItems[i]);
                else System.out.println(deleteMenuNr[i] + ". " + deleteMenuItems[i]);
            }

            try {
                userChoice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Wrong Datatype");
                sc.next();
            }

            //delete a household
            if (userChoice == 0) {
                System.out.println("Which household do you wanna delete? (choose per NR)");
                int id = sc.nextInt();
                try {
                    hhDao.deleteHousehold(id);
                    System.out.println("Haushalt mit der id: " + id + " wurde gelöscht!");
                } catch (Exception e) {
                    System.out.println("Fehler beim Löschen des Haushalts mit der id: " + id + "\n" + e.getMessage());
                }
            }

            //delete a person
            if (userChoice == 1) {
                System.out.println("Which person do you wanna delete? (choose per NR)");
                int id = sc.nextInt();
                try {
                    personDao.deletePerson(id);
                    System.out.println("org.nermin.at.codersbay.household.model.Person mit der id: " + id + " wurde gelöscht!");
                } catch (Exception e) {
                    System.out.println("Fehler beim Löschen der org.nermin.at.codersbay.household.model.Person mit der id: " + id + "\n" + e.getMessage());
                }
            }

            //delete a pet
            if (userChoice == 2) {
                System.out.println("Which pet do you wanna delete? (choose per NR)");
                int id = sc.nextInt();
                try {
                    petDao.deletePet(id);
                    System.out.println("Haustier mit der id: " + id + " wurde gelöscht!");
                } catch (Exception e) {
                    System.out.println("Fehler beim Löschen des Haustiers mit der id: " + id + "\n" + e.getMessage());
                }
            }

        } while (userChoice != 7);
    }
}