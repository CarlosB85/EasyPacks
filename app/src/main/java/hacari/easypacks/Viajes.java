package hacari.easypacks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Viajes extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viajes);
        // cargar viajes
        final ProgressDialog pg = ProgressDialog.show(Viajes.this, "EasyPacks", "Cargando Viajes...");
        final ListView lv = (ListView)findViewById(R.id.lst_Viajes);
        ParseUser user = ParseUser.getCurrentUser();

        if(user == null){
            Toast.makeText(Viajes.this,"no user",Toast.LENGTH_LONG).show();
        }else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Camion");
            query.whereEqualTo("Camionero",user);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> parseObjects, ParseException e) {
                    if (e==null){
                        //exito
                        final ParseObject camion = parseObjects.get(0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String placa = (String)camion.get("Placa");
                                ((TextView)findViewById(R.id.txtPlaca)).setText(placa.toUpperCase());
                            }
                        });
                        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Viaje");
                        query2.whereEqualTo("Camion",camion);
                        query2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                            if(e==null){
                                //exito
                                //*/*/*/*/*

                                String[] values = new String[parseObjects1.size()];

                                for (int i = 0; i < parseObjects1.size() ; i++) {
                                    values[i] = "De: " + parseObjects1.get(i).get("Ubicacion") + " Para: " + parseObjects1.get(i).get("Destino");
                                }

                                final ArrayList<String> list = new ArrayList<String>();
                                for (int i = 0; i < values.length; ++i) {
                                    list.add(values[i]);
                                }
                                final StableArrayAdapter adapter = new StableArrayAdapter(getApplicationContext(),
                                        android.R.layout.simple_list_item_1, list);
                                lv.setAdapter(adapter);

                                // fin cargar viajes
                                // listener lista

                                lv.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {
                                        seleccionarViaje(position);
                                    }
                                });
                                //*/*/*/*/*
                            }else{
                                //error
                                Toast.makeText(Viajes.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            }
                        });

                    }else{
                        //error
                        Toast.makeText(Viajes.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        pg.dismiss();
        //fin listener lista
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viajes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // listeners

    public void click_crearViaje(View vv){
        Intent crear = new Intent(Viajes.this,RegistrarViaje.class);
        startActivity(crear);
    }

    private void seleccionarViaje(int posicion){
        //recuperarViajes
        Intent viaje = new Intent(Viajes.this,VerViaje.class);
        viaje.putExtra("Viaje",posicion);
        startActivity(viaje);
        }

    // fin listener

    //  ******************** ADAPTER *******************

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
