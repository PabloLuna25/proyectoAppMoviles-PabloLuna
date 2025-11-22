package Data

import Entity.Usuario

object MemoryDataManager: IDataManager {
    private var userList = mutableListOf<Usuario>()

    override fun signup(usuario: Usuario){
        userList.add(usuario)
    }

    override fun login(email: String, password: String): Usuario? {
        for (user in userList) {
            if (user.Email == email && user.Password == password) {
                return user
            }
        }
        return null
    }
}