package com.enigmabridge.create;

import com.enigmabridge.EBUtils;
import com.enigmabridge.comm.EBResponse;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Create UO response from EB.
 *
 * Created by dusanklinec on 30.06.16.
 */
public class EBCreateUOResponse extends EBResponse {
    private long objectId;
    private byte[] certificate;
    private List<byte[]> certificateChain;

    public static abstract class ABuilder<T extends EBCreateUOResponse, B extends EBCreateUOResponse.ABuilder>
            extends EBResponse.ABuilder<T,B>
    {
        public B setObjectId(long objectId) {
            getObj().setObjectId(objectId);
            return getThisBuilder();
        }

        public B setCertificate(byte[] certificate) {
            getObj().setCertificate(certificate);
            return getThisBuilder();
        }

        public B setCertificateChain(List<byte[]> certificateChain) {
            getObj().setCertificateChain(certificateChain);
            return getThisBuilder();
        }

        public abstract T build();
        public abstract B getThisBuilder();
        public abstract T getObj();
    }

    public static class Builder extends EBCreateUOResponse.ABuilder<EBCreateUOResponse, EBCreateUOResponse.Builder> {
        private final EBCreateUOResponse parent = new EBCreateUOResponse();

        @Override
        public EBCreateUOResponse.Builder getThisBuilder() {
            return this;
        }

        @Override
        public EBCreateUOResponse getObj() {
            return parent;
        }

        @Override
        public EBCreateUOResponse build() {
            return parent;
        }
    }

    // Setters

    protected EBCreateUOResponse setObjectId(long objectId) {
        this.objectId = objectId;
        return this;
    }

    protected EBCreateUOResponse setCertificate(byte[] certificate) {
        this.certificate = certificate;
        return this;
    }

    protected EBCreateUOResponse setCertificateChain(List<byte[]> certificateChain) {
        this.certificateChain = certificateChain;
        return this;
    }

    // Getters

    public long getObjectId() {
        return objectId;
    }

    public byte[] getCertificate() {
        return certificate;
    }

    public List<byte[]> getCertificateChain() {
        if (certificateChain == null){
            certificateChain = new LinkedList<byte[]>();
        }
        return certificateChain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EBCreateUOResponse that = (EBCreateUOResponse) o;

        if (objectId != that.objectId) return false;
        if (!Arrays.equals(certificate, that.certificate)) return false;
        return certificateChain != null ? certificateChain.equals(that.certificateChain) : that.certificateChain == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (objectId ^ (objectId >>> 32));
        result = 31 * result + Arrays.hashCode(certificate);
        result = 31 * result + (certificateChain != null ? certificateChain.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EBCreateUOResponse{" +
                "objectId=" + objectId +
                ", certificate=" + EBUtils.byte2hex(certificate) +
                ", certificateChain=" + certificateChain +
                "} " + super.toString();
    }
}
