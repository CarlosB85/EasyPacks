package hacari.easypacks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class Login extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "QkvR6KcQKJMoSBM181eUmcTeaYOedEIZ7fslE9oZ", "r0J0DzLlU6oICz0rBfUCYs9qZDOssSb3pZ55rO3v");
        }catch (Exception e){Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}
    }

    public void login(View vv){
        final EditText usernameET = (EditText) findViewById(R.id.txtUsuario);
        final EditText passET = (EditText) findViewById(R.id.txtPass);

        final ProgressDialog pg = ProgressDialog.show(Login.this,"EasyPacks Login","Iniciando Sesion");

                ParseUser.logInInBackground(usernameET.getText().toString(), passET.getText()
                        .toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        pg.dismiss();
                        if (user != null) {
                            Toast.makeText(getApplicationContext(), "Autenticado", Toast.LENGTH_SHORT).show();
                            Intent viajes = new Intent(Login.this, Viajes.class);
                            startActivity(viajes);
                        } else {
                            Toast.makeText(getApplicationContext(), "Erroneo", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}