package com.example.intenttest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {

    private EditText telefone = null;
    private EditText email = null;
    private EditText nome = null;
    private Usuario novo;
    static final int criarCont = 1;
    static final int mandarMsg = 2;
    static final int mandarEmail = 3;
    private Button btnTelaLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTelaLogin = (Button) findViewById(R.id.btnLogin);
        btnTelaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telaLogin();
            }
        });
    }

    public void salvarDados(View view){
        telefone = (EditText) findViewById(R.id.editTextTelefone);
        email = (EditText) findViewById(R.id.editTextEmail);
        nome = (EditText) findViewById(R.id.editTextNome);

        String t = telefone.getText().toString();
        String n = nome.getText().toString();
        String e = email.getText().toString();

        novo = new Usuario(n, e, t);

        Intent criarContato = new Intent(ContactsContract.Intents.Insert.ACTION);
        criarContato.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        criarContato.putExtra(ContactsContract.Intents.Insert.EMAIL, novo.getEmail()); // adiciona o email
        criarContato.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK); // muda o tipo de email
        criarContato.putExtra(ContactsContract.Intents.Insert.PHONE, novo.getTelefone()); // adiciona o telefone
        criarContato.putExtra(ContactsContract.Intents.Insert.NAME, novo.getNome());
        criarContato.putExtra("finishActivityOnSaveCompleted", true);

        startActivityForResult(criarContato, criarCont);

    }


    public void enviarWpp(){
        PackageManager packageManager = this.getPackageManager();
        Intent enviarZap = new Intent(Intent.ACTION_VIEW);
        String phone = "5531" + novo.getTelefone();

        try {
            String url = "https://api.whatsapp.com/send?phone="+ phone +"&text=" + URLEncoder.encode("Cadastro realizado, " + novo.getNome(), "UTF-8");
            enviarZap.setPackage("com.whatsapp");
            enviarZap.setData(Uri.parse(url));

            if (enviarZap.resolveActivity(packageManager) != null) {
                this.startActivityForResult(enviarZap, mandarMsg);
            } else {
                Toast.makeText(this.getApplicationContext(),"Mensagem nao enviada",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void enviarEmail() {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { novo.getEmail()});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Confirmação");
            intent.putExtra(Intent.EXTRA_TEXT, "Cadastro Realizado");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, mandarEmail);
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(MainActivity.this.getApplicationContext(),"Enviado com sucesso.",Toast.LENGTH_SHORT).show();
        if(requestCode == mandarMsg) {
            if (resultCode==RESULT_OK) {
                criarDialog(2);
            }
        }

        if(requestCode == mandarEmail){
            if(resultCode==RESULT_OK){
                criarDialog(3);
            }
        }

        if(requestCode == criarCont){
            if(resultCode==RESULT_OK){
                criarDialog(1);
            }
            else if(resultCode==RESULT_CANCELED){
                Toast.makeText(MainActivity.this.getApplicationContext(),"Cadastro não realizado.",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void criarDialog(final int passoAtual){
        String mensagem = "", titulo = "";
        if(passoAtual == 1){
            mensagem = "Contato criado.";
        }
        if(passoAtual == 2){
            mensagem = "Mensagem enviada.";
        }
        if(passoAtual == 3){
            mensagem = "E-mail enviado.";
        }

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(mensagem);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Prosseguir",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(passoAtual == 1){
                            enviarWpp();
                        }
                        if(passoAtual == 2){
                            enviarEmail();
                        }
                        if(passoAtual == 3){
                            dialog.cancel();
                        }
                    }
                });

        builder1.setNegativeButton(
                "Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void telaLogin(){
        Intent loginScreen = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(loginScreen);
    }
}
