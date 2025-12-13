package Controller

import Data.IDataManager
import Data.MemoryDataManager
import Entity.Usuario
import android.content.Context
import com.example.pulsetasks.R

class UserController {
    private var dataManager: IDataManager = MemoryDataManager
    private var context: Context

    constructor(context: Context) {
        this.context = context
    }

    fun signup(user: Usuario){
        try {
            dataManager.signup(user)
        }catch (e: Exception){
            throw Exception(context.getString(R.string.ErrorMsgSignup))
        }
    }

    fun login(email: String, password: String){
        try {
            dataManager.login(email, password)
        }catch (e: Exception){
            throw Exception(context.getString(R.string.ErrorMsgLogin))
        }
    }

}