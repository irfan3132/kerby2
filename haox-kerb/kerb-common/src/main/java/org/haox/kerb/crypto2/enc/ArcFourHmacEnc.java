package org.haox.kerb.crypto2.enc;

import org.haox.kerb.crypto2.ArcFourHmac;
import org.haox.kerb.crypto2.key.KeyMaker;
import org.haox.kerb.crypto2.key.Rc4KeyMaker;
import org.haox.kerb.spec.KrbException;
import org.haox.kerb.spec.type.common.CheckSumType;
import org.haox.kerb.spec.type.common.EncryptionType;

import java.security.GeneralSecurityException;

public final class ArcFourHmacEnc extends AbstractEncryptionTypeHandler {

    public ArcFourHmacEnc() {
        super(new Rc4KeyMaker());
    }

    public EncryptionType eType() {
        return EncryptionType.ARCFOUR_HMAC;
    }

    public int minimumPadSize() {
        return 1;
    }

    public int confounderSize() {
        return 8;
    }

    public CheckSumType checksumType() {
        return CheckSumType.HMAC_MD5_ARCFOUR;
    }

    public int checksumSize() {
        return ArcFourHmac.getChecksumLength();
    }

    public int blockSize() {
        return 1;
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
            return ArcFourHmac.encrypt(key, usage, ivec, data, 0, data.length);
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
            return ArcFourHmac.decrypt(key, usage, ivec, cipher, 0, cipher.length);
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
