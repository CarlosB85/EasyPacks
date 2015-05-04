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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class VerViaje extends ActionBarActivity {

    private ParseObject viaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_viaje);
        // carga de datos
        final int posicion = getIntent().getIntExtra("Viaje",0);
        final ProgressDialog pg = ProgressDialog.show(VerViaje.this,"EasyPacks","Cargando Elementos...");

        ParseUser user = ParseUser.getCurrentUser();

        if(user == null){
            Toast.makeText(VerViaje.this, "no user", Toast.LENGTH_LONG).show();
        }else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Camion");
            query.whereEqualTo("Camionero",user);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> parseObjects, ParseException e) {
                    if (e==null){
                        //exito

                        final ParseObject camion = parseObjects.get(0);

                        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Viaje");
                        query2.whereEqualTo("Camion",camion);
                        query2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects1, ParseException e) {
                                if(e==null){
                                    //exito
                                    //*/*/*/*/*

                                    viaje = parseObjects1.get(posicion);
                                    pg.dismiss();

                                    if (viaje!=null){
                                        //llenar etiquetas.
                                        String origen = viaje.getString("Ubicacion");
                                        String destino = viaje.getString("Destino");
                                        ((TextView)findViewById(R.id.txtVerViajeOrigen)).setText(origen);
                                        ((TextView)findViewById(R.id.txtVerViajeDestino)).setText(destino);
                                        final ListView lv = (ListView) findViewById(R.id.lst_Ofertas);

                                        //query de ofertas
                                        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Oferta");
                                        query3.whereEqualTo("Viaje",viaje);
                                        query3.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> parseObjects2, ParseException e) {
                                                if (e==null){

                                                    // llenado de lista
                                                    String[] values = new String[parseObjects2.size()];

                                                    for (int i = 0; i < parseObjects2.size() ; i++) {
                                                        String preContenido = (String)parseObjects2.get(i).get("Contenido");
                                                        String contenido = preContenido;
                                                        if (preContenido.length()>30){
                                                            contenido = preContenido.substring(0,30)+"...";
                                                        }
                                                        values[i] = "Peso : " + parseObjects2.get(i).get("Peso") + " Contenido : " + contenido;

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

                                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                                int position, long id) {
                                                            //seleccionarViaje(position);
                                                        }
                                                    });
                                                    //fin listener lista
                                                    //fin llenado de lista
                                                }else{
                                                    //error
                                                    Toast.makeText(VerViaje.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    }else{
                                        Intent viaje = new Intent(VerViaje.this,Viajes.class);
                                        startActivity(viaje);
                                    }

                                    //*/*/*/*/*
                                }else{
                                    //error
                                    Toast.makeText(VerViaje.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        //error
                        Toast.makeText(VerViaje.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ver_viaje, menu);
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
