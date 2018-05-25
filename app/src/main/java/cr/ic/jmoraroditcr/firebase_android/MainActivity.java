package cr.ic.jmoraroditcr.firebase_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Productos");
    private FirebaseAuth mAuth;
    private ArrayList<Item> items = new ArrayList<>();
    private ListView list;
    private ArrayAdapter adapter;
    private ArrayList<String> productosString;

    public void agregar_producto(View view){
        Intent intent = new Intent(this, AgregarProducto.class);
        startActivity(intent);
    }
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
                productosString = new ArrayList<String>();
                int i = 0;
                for(Item item : items)
                {
                    Log.d("For", item.getNombre());
                    productosString.add(item.getNombre());
                    i++;
                }


                adapter = new ArrayAdapter<String>(MainActivity.this,
                        R.layout.list_layout, productosString);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {


                        Intent intent = new Intent(getApplicationContext(), DetalleItem.class);

                        intent.putExtra("nombre", items.get(position).getNombre());
                        intent.putExtra("precio", items.get(position).getPrecio());
                        intent.putExtra("descripcion", items.get(position).getDescripcion());
                        intent.putExtra("foto", items.get(position).getFoto());
                        startActivity(intent);


                    }
                });
                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   final int pos, long id) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Está seguro?")
                                .setMessage("Desea eliminar el producto?")
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Productos");
                                        Query query = ref.orderByChild("nombre").equalTo(items.get(pos).getNombre());
                                        Log.d("Producto a borrar",items.get(pos).getNombre());
                                        Log.d("DebugAlertDialog","Antes del listener");
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                for (DataSnapshot productoSnapshot: dataSnapshot.getChildren()){
                                                    Log.d("productoABorrar", productoSnapshot.getValue().toString());
                                                    productoSnapshot.getRef().removeValue();
                                                    productosString.remove(items.get(pos).getNombre());
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.e("Canceled", "onCancelled", databaseError.toException());
                                            }
                                        });

                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();

                        return true;
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Data", "Failed to read value.", error.toException());
            }
        });

    }

}
