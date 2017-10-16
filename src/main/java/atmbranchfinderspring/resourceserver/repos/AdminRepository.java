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

    public Boolean persistData () {

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(PATH))){
            os.writeObject(admins);
            os.close();
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

    public Boolean loadData() {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(PATH))) {
            admins = null;
            admins = (HashMap<String, Admin>) is.readObject();
            is.close();
            return true;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public void add(Admin entity) {
    	System.out.println(entity.getAdminId());
        admins.put(entity.getAdminId(), entity);
        System.out.println(admins.size());
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
    public boolean contains(String id) {
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
