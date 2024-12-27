package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel

@Composable
fun CriarPerguntaScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    tipoPerguntaSelecionada: Int
){
    var nome by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = nome,
            isError = nome.isEmpty(),
            label = {
                Text("Pergunta:")
            },
            onValueChange = { newText ->
                nome = newText
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        when(tipoPerguntaSelecionada){
            0 -> {
                Text("Soluçao: ")
                Column{
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var isChecked  by remember { mutableStateOf<Boolean?>(null) }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){
                            Text("Verdadeiro")
                            Checkbox(
                                checked = isChecked  == true,
                                onCheckedChange = { isChecked  =  if (it) true else null}
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){
                            Text("Falso")
                            Checkbox(
                                checked = isChecked  == false,
                                onCheckedChange = { isChecked  =  if (it) false else null }
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
        },modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text("Guardar")
        }
    }
}