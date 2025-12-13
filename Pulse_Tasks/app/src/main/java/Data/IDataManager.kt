package Data

import Entity.Usuario

interface IDataManager {
    fun signup (usuario: Usuario)
    fun login (email: String, password: String): Usuario?
}
