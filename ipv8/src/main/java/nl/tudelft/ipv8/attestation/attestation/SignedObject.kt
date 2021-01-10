package nl.tudelft.ipv8.attestation.attestation

import nl.tudelft.ipv8.keyvault.PrivateKey
import nl.tudelft.ipv8.keyvault.PublicKey
import nl.tudelft.ipv8.keyvault.defaultCryptoProvider
import nl.tudelft.ipv8.util.sha3_256

abstract class SignedObject(privateKey: PrivateKey? = null, signature: ByteArray? = null) {

    var hash = byteArrayOf()
    lateinit var signature: ByteArray;
    private val crypto = defaultCryptoProvider


    init {
        this._sign(privateKey, signature)
    }

    fun verify(publicKey: PublicKey): Boolean {
        return publicKey.verify(signature, this.getPlaintext())
    }

    private fun _sign(privateKey: PrivateKey? = null, signature: ByteArray? = null) {
        if (privateKey != null && signature == null) {
            privateKey.sign(this.getPlaintext())
        } else if (privateKey == null && signature != null) {
            this.signature = signature
        } else {
            throw RuntimeException("Specify either a private key or a signature.")
        }

        this.hash = sha3_256(this.getPlaintextSigned())

    }

    abstract fun getPlaintext(): ByteArray

    fun getPlaintextSigned(): ByteArray {
        return this.getPlaintext() + this.signature
    }

    companion object {
        fun deserialize(data: ByteArray, publicKey: PublicKey, offset: Int = 0): Any {
            throw RuntimeException("This method must be overridden.")
        }
    }

    // TODO: override `equals` and `hashcode`.
}
