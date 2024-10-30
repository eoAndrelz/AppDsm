package com.example.appdsm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.appdsm.roomDB.Pessoa
import com.example.appdsm.roomDB.PessoaDatabase
import com.example.appdsm.ui.theme.AppDsmTheme
import com.example.appdsm.viewModel.PessoaViewModel
import com.example.appdsm.viewModel.Repository

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            PessoaDatabase::class.java,
            "pessoas.db"
        ).build()
    }

    private val viewModel by viewModels<PessoaViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PessoaViewModel(Repository(db)) as T
                }
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppDsmTheme {
                @OptIn(ExperimentalMaterial3Api::class)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(viewModel, this)

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable


fun App(viewModel: PessoaViewModel, mainActivity: MainActivity){

    var nome by remember{
        mutableStateOf("")
    }
    var telefone by remember{
        mutableStateOf("")
    }
    val pessoa = Pessoa(
        nome,
        telefone
    )

    var pessoaList by remember{
        mutableStateOf(listOf<Pessoa>())
    }


    viewModel.getPessoa().observe(mainActivity){
        pessoaList = it
    }



    Column(
        Modifier
            .background(Color.Black)
            .fillMaxHeight()
            .fillMaxWidth()
    )
    {
        Row(Modifier.padding(20.dp)) {}
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.Center
        ) {
            Text(
                "Aula DSM",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color.White
            )
        }
        Row(Modifier.padding(20.dp)) {}

        Row(Modifier.fillMaxWidth(), Arrangement.Center) {
            Column(Modifier.fillMaxWidth(0.2f)) {
                Text(
                    "Nome: ",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
            Column {
                TextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { },
                    modifier = Modifier.height(56.dp)
                )
            }
        }


        Row(Modifier.padding(20.dp)) {}

        Row(Modifier.fillMaxWidth(), Arrangement.Center)
        {
            Column(Modifier.fillMaxWidth(0.2f)) {
                Text(
                    "Telefone: ",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
            Column() {
                TextField(
                    value = telefone,
                    onValueChange = {telefone = it},
                    label = { },
                    modifier = Modifier.height(56.dp)
                )
            }
        }
        Row(
            Modifier
                .padding(20.dp)
        ) {
        }

        Row(Modifier.fillMaxWidth(), Arrangement.Center) {
            Button(
                onClick = {
                    viewModel.upsertPessoa(pessoa)
                    nome = ""
                    telefone = ""
                }
            ) {
                Text(
                    "Cadastrar",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }

        Row(
            Modifier
                .padding(20.dp)
        ) {
        }

        Divider()
        Row(
            Modifier
                .padding(20.dp)
        ) {}
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.Center
        ){
            Column(
                Modifier.fillMaxWidth(0.5f),
                Arrangement.Center
            ){
                Text("Nomes", color = Color.White)
            }
            Column( Modifier.fillMaxWidth(0.5f),
                Arrangement.Center
            ){
                Text("Telefones", color = Color.White)
            }
        }

        LazyColumn {
            items(pessoaList){ pessoa ->
                Row(
                    Modifier.fillMaxWidth()
                        .clickable {
                            viewModel.deletePessoa(pessoa)
                        },
                    Arrangement.Center
                ){
                    Column(
                        Modifier.fillMaxWidth(0.5f),
                        Arrangement.Center
                    ){
                        Text(text = "${pessoa.nome}", color = Color.White)
                    }
                    Column( Modifier.fillMaxWidth(0.5f),
                        Arrangement.Center
                    ){
                        Text(text = "${pessoa.telefone}", color = Color.White)
                    }
                }
            }
        }
    }
}