package cr.ic.jmoraroditcr.firebase_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Productos");
    private FirebaseAuth mAuth;
    ArrayList<Item> items = new ArrayList<>();
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = findViewById(R.id.lista_productos);
        mAuth = FirebaseAuth.getInstance();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Item item1 = productSnapshot.getValue(Item.class);

                    String name = item1.getNombre();
                    Log.d("Debug",name);
                    String precio = productSnapshot.getValue(Item.class).getPrecio();
                    String foto = productSnapshot.getValue(Item.class).getFoto();
                    String descripcion = productSnapshot.getValue(Item.class).getDescripcion();
                    String producto_id = productSnapshot.getKey();
                    String userId = productSnapshot.getValue(Item.class).getID_Usuario();



                    FirebaseUser user = mAuth.getCurrentUser();
                    String uid = user.getUid();
                    try {
                        if (uid.compareTo(item1.getID_Usuario()) == 0) {
                            items.add(item1);
                        }
                    }catch (Exception ex){
                        Log.d("Exception",ex.toString());
                    }

                }
                Log.d("Debug","Hola");
                String[] productosString = new String[items.size()];
                int i = 0;
                for(Item item : items)
                {
                    Log.d("For", item.getNombre());
                    productosString[i] = item.getNombre();
                    i++;
                }


                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this,
                        R.layout.list_layout, productosString);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        /*
                        Intent intent = new Intent(getApplicationContext(), ItemDetail.class);

                        intent.putExtra("nombre", items.get(position).getNombre());
                        intent.putExtra("precio", items.get(position).getPrecio());
                        intent.putExtra("descripcion", items.get(position).getDescripcion());
                        intent.putExtra("foto", items.get(position).getFoto());
                        startActivity(intent);

                        */
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Data", "Failed to read value.", error.toException());
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
