package atmbranchfinderspring.resourceserver.repos;

import atmbranchfinderspring.resourceserver.models.Admin;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;

@org.springframework.stereotype.Repository
public class AdminRepository implements Repository<Admin>{

    private HashMap<String, Admin> admins;
    private static final String PATH = "src/main/resources/admins.txt";

    public AdminRepository() {
        this.admins = new HashMap<>();
    }

    public void persistData () {

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(PATH))){
            os.writeObject(admins);
            os.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void loadData() {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(PATH))) {
            admins = null;
            admins = (HashMap<String, Admin>) is.readObject();
            is.close();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    @Override
    public void add(Admin entity) {
        admins.put(entity.getAdminId(), entity);
    }

    @Override
    public Admin get(String id) {
        return admins.get(id);
    }

    @Override
    public Collection<String> getAllIds() {
        return admins.keySet();
    }

    @Override
    public Boolean contains(String id) {
        return admins.containsKey(id);
    }

    @Override
    public void delete(Admin entity) {
        admins.remove(entity.getAdminId(), entity);
    }

    @Override
    public void delete(String id) {
        admins.remove(id);
    }
}
