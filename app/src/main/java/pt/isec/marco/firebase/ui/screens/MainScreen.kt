package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSignOut: () -> Unit,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    viewModel: FirebaseViewModel

) {
    val context = LocalContext.current
    val currentScreen by navController.currentBackStackEntryAsState()
    var tipoPerguntaSelecionada by remember { mutableStateOf(-1) }
    var showComplete by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Firebase App")
                },
                actions = {
                    when(currentScreen?.destination?.route){
                        "criar-questionario" ->{
                            IconButton(
                                onClick = {
                                    showComplete = !showComplete
                                }
                            ) {
                                Icon(
                                    Icons.Filled.ArrowDropDown,
                                    contentDescription = "Save question"
                                )
                            }
                        }
                        else -> {
                            IconButton(
                                onClick = {
                                    onSignOut()
                               }
                           ) {
                               Icon(
                                   imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                   contentDescription = "Sign out"
                               )
                           }
                       }
                    }

                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            navController.navigate("firestore") {
                                popUpTo("firestore") {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Firestore",
                            tint = if (currentScreen?.destination?.route == "firestore") Color(0,128,0) else Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            navController.navigate("intent") {
                                popUpTo("intent") {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Intent",
                            tint = if (currentScreen?.destination?.route == "intent") Color(0,128,0) else Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "quizec",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = "quizec"){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(108, 147, 201, 255))
                ){
                    QuizecScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
            composable(route = "menu-criador") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(108, 147, 201, 255))
                ){
                    CriadorMenuScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
            composable(route = "criar-questionario") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(108, 147, 201, 255))
                ){
                    CriarQuestionarioScreen(
                        viewModel = viewModel,
                        navController = navController,
                        showComplete = showComplete
                    )

                }
            }
            composable(route = "tipo-pergunta") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(108, 147, 201, 255))
                ){
                    TipoPerguntaScreen(
                        viewModel = viewModel,
                        navController = navController,
                        onPerguntaSelected = { index ->
                            tipoPerguntaSelecionada = index
                        }
                    )
                }
            }
            composable(route = "criar-pergunta") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(108, 147, 201, 255))
                ){
                    CriarPerguntaScreen(
                        viewModel = viewModel,
                        navController = navController,
                        tipoPerguntaSelecionada = tipoPerguntaSelecionada
                    )
                }
            }
            composable(route = "menu-utilizador") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(108, 147, 201, 255))
                ){
                    UtilizadorMenuScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
            composable(route = "responder-questionario") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(108, 147, 201, 255))
                ){
                    ResponderQuestionarioScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
            composable(route = "seleciona-perguntas") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(108, 147, 201, 255))
                ){
                    SelecionarPerguntasScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
            composable(route = "ver-questionario") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(108, 147, 201, 255))
                ){
                    VerQuestionarioScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

