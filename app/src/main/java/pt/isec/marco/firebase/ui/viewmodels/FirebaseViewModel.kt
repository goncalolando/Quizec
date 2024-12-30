package pt.isec.marco.firebase.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import pt.isec.marco.firebase.utils.FAuthUtil
import pt.isec.marco.firebase.utils.FAuthUtil.Companion.currentUser
import pt.isec.marco.firebase.utils.FStorageUtil

open class FirebaseViewModel : ViewModel() {
    val partilhas = mutableStateOf(listOf<String>())
    private val _user = mutableStateOf(FAuthUtil.currentUser?.toUser())
    val perguntas = mutableStateOf(listOf<String>())
    val questionarios = mutableStateOf(listOf<String>())

    open val user : State<Utilizador?>
        get() = _user

    private val _error = mutableStateOf<String?>(null)
    open val error : State<String?>
        get() = _error

    fun createUserWithEmail(email: String, password: String) {
        if (email.isBlank() && password.isBlank())
            return;
        viewModelScope.launch {
            FAuthUtil.createUserWithEmail(email, password) { exception ->
                if (exception == null) {
                    _user.value = FAuthUtil.currentUser?.toUser()
                }
                _error.value = exception?.message
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isBlank() && password.isBlank())
            return;
        viewModelScope.launch {
            FAuthUtil.signInWithEmail(email, password) { exception ->
                if (exception == null) {
                    _user.value = FAuthUtil.currentUser?.toUser()
                }
                _error.value = exception?.message
            }
        }
    }

    fun signOut() {
        FAuthUtil.signOut()
        _user.value = null
        _error.value = null
    }
    private val _descricao = mutableStateOf("")
    val descricao: State<String>
        get() = _descricao

    private val _perguntas = mutableStateOf(emptyList<Pergunta>())
    val perguntass: State<List<Pergunta>>
        get() = _perguntas


    private val _nrgames = mutableLongStateOf(0L)
    open val nrgames : State<Long>
        get() = _nrgames

    private val _topscore = mutableLongStateOf(0L)
    open val topscore : State<Long>
        get() = _topscore

    fun addDataToFirestore() {
        viewModelScope.launch {
            FStorageUtil.addDataToFirestore { exception ->
                _error.value = exception?.message
            }
        }
    }

    fun addPerguntaToFirestore(pergunta: Pergunta) {
        viewModelScope.launch {
            FStorageUtil.addPerguntaToFirestore({ exception ->
                _error.value = exception?.message
            }, pergunta, this@FirebaseViewModel)
        }
    }

    fun addQuestioanrioToFirestore(questionario: Questionario) {
        viewModelScope.launch {
            FStorageUtil.addQuestionarioToFirestore({ exception ->
                _error.value = exception?.message
            }, questionario, this@FirebaseViewModel)
        }
    }

    fun addPartilhaToFirestore(partilha: Partilha) {
        viewModelScope.launch {
            FStorageUtil.addPartilhaToFirestore({ exception ->
                _error.value = exception?.message
            }, partilha, this@FirebaseViewModel)
        }
    }

    fun updateDataInFirestore() {
        viewModelScope.launch {
            //FirebaseUtils.updateDataInFirestore()
            FStorageUtil.updateDataInFirestoreTrans { exception ->
                _error.value = exception?.message
            }
        }
    }

    fun removeDataFromFirestore() {
        viewModelScope.launch {
            FStorageUtil.removeDataFromFirestore { exception ->
                _error.value = exception?.message
            }
        }
    }
    private val _questionariosAux = mutableStateOf<List<Questionario>>(emptyList()) // Lista de questionários
    val questionariosAux: State<List<Questionario>> get() = _questionariosAux
    fun startObserver() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            viewModelScope.launch {
                // Chama o método de observação passando o userId
                FStorageUtil.startObserver(userId) { questionarios, throwable ->
                    if (throwable != null) {
                        Log.e("Firestore", "Erro ao observar questionários: ${throwable.message}")
                    } else {
                        questionarios?.let {
                            // Armazena todos os questionários que foram recuperados
                            _questionariosAux.value = it // Atualiza a lista de questionários
                        }
                    }
                }
            }
        } else {
            Log.e("Firestore", "Utilizador não autenticado")
        }
    }


//    fun startObserver() {
//        viewModelScope.launch {
//            FStorageUtil.startObserver { g, t ->
//                _nrgames.longValue = g
//                _topscore.longValue = t
//            }
//        }
//    }


    fun stopObserver() {
        viewModelScope.launch {
            FStorageUtil.stopObserver()
        }
    }

    private val _questionarios_criados = mutableStateOf(listOf<Questionario>())



}