package hacari.easypacks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;


public class RegistrarViaje extends ActionBarActivity {

    private String ubicacion;
    private String destino;
    private String paradas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_viaje);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registrar_viaje, menu);
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

    public void click_siguiente(View vv){
        //verificarEntradas
        ubicacion = ((EditText)findViewById(R.id.txtUbicacion)).getText().toString();
        destino = ((EditText)findViewById(R.id.txtDestino)).getText().toString();
        paradas = ((EditText)findViewById(R.id.txtParadas)).getText().toString();
        if(paradas.trim().equals("")){
            paradas = "Ninguna";
        }
        if (ubicacion.trim().equals("") || destino.trim().equals("")){
            Toast.makeText(getApplicationContext(),"La ubicacion o destino estan mal.",Toast.LENGTH_LONG).show();
        }else {
            setContentView(R.layout.activity_registrar_viaje2);
        }
    }

    public void click_crearViaje(View vv){
        final String volumen = ((EditText)findViewById(R.id.txtVolumen)).getText().toString();
        final Date fecha = new Date(((DatePicker)findViewById(R.id.dtPick)).getCalendarView().getDate());
        final ProgressDialog pg = ProgressDialog.show(RegistrarViaje.this,"EasyPacks","Registrando");

        ParseUser user = ParseUser.getCurrentUser();

        if(user == null){
            Toast.makeText(RegistrarViaje.this,"no user",Toast.LENGTH_LONG).show();
        }else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Camion");
            query.whereEqualTo("Camionero", user);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null) {
                        //exito

                                    ParseObject viaje = new ParseObject("Viaje");
                                    viaje.put("Ubicacion", ubicacion);
                                    viaje.put("Destino", destino);
                                    viaje.put("Paradas", paradas);
                                    viaje.put("Volumen", volumen);
                                    viaje.put("FechaSalida", fecha);
                                    viaje.put("Camion",parseObjects.get(0));
                                    viaje.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            pg.dismiss();
                                            if (e == null) {

                                                Toast.makeText(getApplicationContext(), "Registro exitoso.", Toast.LENGTH_SHORT).show();
                                                try {
                                                    Thread.sleep(1500);
                                                } catch (InterruptedException ee) {
                                                    e.printStackTrace();
                                                }
                                                Intent back = new Intent(RegistrarViaje.this, Viajes.class);
                                                startActivity(back);
                                            } else {
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                    } else {
                        //error
                        Toast.makeText(RegistrarViaje.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

}