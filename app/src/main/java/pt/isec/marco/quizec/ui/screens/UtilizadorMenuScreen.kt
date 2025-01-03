package pt.isec.marco.quizec.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.isec.marco.quizec.ui.viewmodels.FirebaseViewModel

@Composable
fun UtilizadorMenuScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("User: ${viewModel.user.value?.email ?: ""}")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Menu Utilizador")
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("historico-questionarios") {
                    popUpTo("historico-questionarios") {
                        inclusive = true
                    }
                }
            }
        ) {
            Text("Historico questionarios")
        }
        Button(
            onClick = {
                navController.navigate("responder-questionario") {
                popUpTo("responder-questionario") {
                    inclusive = true
                }
            }}
        ) {
            Text("Responder questionario")
        }
    }
}