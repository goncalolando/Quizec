package pt.isec.marco.firebase.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import pt.isec.marco.firebase.utils.FAuthUtil
import pt.isec.marco.firebase.utils.FStorageUtil

data class User(val name: String, val email: String,val picture: String?)
data class Pergunta(val id: String, val titulo: String, val imagem: String, val respostas: List<String>, val respostaCerta: List<String>)
data class Questionario(val id: String, val descricao: String, val perguntas: List<Pergunta>)

fun FirebaseUser.toUser(): User {
    val displayName = this.displayName?: ""
    val strEmail = this.email?: ""
    val picture = this.photoUrl?.toString()
    return User(displayName, strEmail, picture)
}

open class FirebaseViewModel : ViewModel() {
    private val _user = mutableStateOf(FAuthUtil.currentUser?.toUser())
    open val user : State<User?>
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

    fun startObserver() {
        viewModelScope.launch {
            FStorageUtil.startObserver { g, t ->
                _nrgames.longValue = g
                _topscore.longValue = t
            }
        }
    }

    fun stopObserver() {
        viewModelScope.launch {
            FStorageUtil.stopObserver()
        }
    }

    private val _questionarios_criados = mutableStateOf(listOf<Questionario>())


}