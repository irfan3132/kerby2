package org.haox.kerb.crypto2.enc;

import org.haox.kerb.crypto2.Aes128;
import org.haox.kerb.crypto2.key.Aes128KeyMaker;
import org.haox.kerb.crypto2.key.KeyMaker;
import org.haox.kerb.spec.KrbException;
import org.haox.kerb.spec.type.common.CheckSumType;
import org.haox.kerb.spec.type.common.EncryptionType;

import java.security.GeneralSecurityException;

public final class Aes128CtsHmacSha1Enc extends AbstractEncryptionTypeHandler {

    public Aes128CtsHmacSha1Enc() {
        super(new Aes128KeyMaker());
    }

    public EncryptionType eType() {
        return EncryptionType.AES128_CTS_HMAC_SHA1_96;
    }

    public int minimumPadSize() {
        return 0;
    }

    public int confounderSize() {
        return blockSize();
    }

    public CheckSumType checksumType() {
        return CheckSumType.HMAC_SHA1_96_AES128;
    }

    public int checksumSize() {
        return Aes128.getChecksumLength();
    }

    public int blockSize() {
        return 16;
    }

    public int keySize() {
        return 16; // bytes
    }

    public byte[] encrypt(byte[] data, byte[] key, int usage)
        throws KrbException {
        byte[] ivec = new byte[blockSize()];
        return encrypt(data, key, ivec, usage);
    }

    public byte[] encrypt(byte[] data, byte[] key, byte[] ivec, int usage)
        throws KrbException {
        try {
            return Aes128.encrypt(key, usage, ivec, data, 0, data.length);
        } catch (GeneralSecurityException e) {
            KrbException ke = new KrbException(e.getMessage());
            ke.initCause(e);
            throw ke;
        }
    }

    public byte[] decrypt(byte[] cipher, byte[] key, int usage)
        throws KrbException {
        byte[] ivec = new byte[blockSize()];
        return decrypt(cipher, key, ivec, usage);
    }

    public byte[] decrypt(byte[] cipher, byte[] key, byte[] ivec, int usage)
        throws KrbException {
        try {
            return Aes128.decrypt(key, usage, ivec, cipher, 0, cipher.length);
        } catch (GeneralSecurityException e) {
            KrbException ke = new KrbException(e.getMessage());
            ke.initCause(e);
            throw ke;
        }
    }

    // Override default, because our decrypted data does not return confounder
    // Should eventually get rid of EncType.decryptedData and
    // EncryptedData.decryptedData altogether
    public byte[] decryptedData(byte[] data) {
        return data;
    }
}
