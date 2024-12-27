package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel

@Composable
fun CriarQuestionarioScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController
) {
    Column() {
        Text("LISTA DE PERGUNTAS")
        Button(
            onClick = { navController.navigate("criar-pergunta")
            {
                popUpTo("criar-pergunta") {
                    inclusive = true
                }
            }
            }
        ) {
            Text("Criar pergunta")
        }
    }
}