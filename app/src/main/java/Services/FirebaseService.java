package Services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import Models.Order;
import Models.User;

public class FirebaseService {
    private FirebaseDatabase firebaseDatabase;
    Order order;
    User user;

    public FirebaseService() {
        firebaseDatabase=FirebaseDatabase.getInstance();

    }

    public void saveUser(User user){


    }




}
