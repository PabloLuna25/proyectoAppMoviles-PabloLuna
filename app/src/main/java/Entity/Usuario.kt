package Entity

import android.graphics.Bitmap

class Usuario {
    private var id: String = ""
    private var nombre: String = ""
    private var primerApellido: String = ""
    private var segundoApellido: String = ""
    private var phoneNum: Int = 0
    private var email: String = ""
    private var password: String = ""
    private var photo: Bitmap? = null

    constructor(
        id: String,
        nombre: String,
        primerApellido: String,
        segundoApellido: String,
        phoneNum: Int,
        email: String,
        password: String,
        photo: Bitmap?
    ){
        this.id = id
        this.nombre = nombre
        this.primerApellido=primerApellido
        this.segundoApellido=segundoApellido
        this.phoneNum=phoneNum
        this.email=email
        this.password=password
        this.photo=photo
    }

    var ID: String
        get() = this.id
        set(value) {
            this.id = value
        }

    var Nombre: String
        get() = this.nombre
        set(value) {
            this.nombre = value
        }

    var PrimerApellido: String
        get() = this.primerApellido
        set(value) {
            this.primerApellido = value
        }

    var SegundoApellido: String
        get() = this.segundoApellido
        set(value) {
            this.segundoApellido = value
        }

    var PhoneNum: Int
        get() = this.phoneNum
        set(value) {
            this.phoneNum = value
        }

    var Email: String
        get() = this.email
        set(value) {
            this.email = value
        }

    var Password: String
        get() = this.password
        set(value) {
            this.password = value
        }

    var Photo: Bitmap?
        get() = this.photo
        set(value) {
            this.photo = value
        }
}
