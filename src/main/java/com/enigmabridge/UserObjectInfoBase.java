package com.enigmabridge;

import java.io.*;

/**
 * Holder represents user object and all required parameters for using it.
 *
 * Created by dusanklinec on 21.04.16.
 */
public class UserObjectInfoBase implements UserObjectInfo {
    public static final long serialVersionUID = 1L;

    /**
     * User object handle.
     */
    protected long uoid;

    /**
     * Type of the user object.
     * Required for API token build.
     */
    protected long userObjectType;

    /**
     * Communication keys.
     */
    protected EBCommKeys commKeys = new EBCommKeys();

    /**
     * API key for using EB service.
     */
    protected String apiKey;

    /**
     * Connection string to the EB endpoint
     * https://site1.enigmabridge.com:11180
     */
    protected EBEndpointInfo endpointInfo;

    public UserObjectInfoBase() {
    }

    public UserObjectInfoBase(long uoid) {
        this.uoid = uoid;
    }

    public UserObjectInfoBase(long uoid, byte[] encKey, byte[] macKey) {
        this.uoid = uoid;
        this.commKeys.encKey = encKey;
        this.commKeys.macKey = macKey;
    }

    public UserObjectInfoBase(long uoid, byte[] encKey, byte[] macKey, String apiKey) {
        this.uoid = uoid;
        this.commKeys.encKey = encKey;
        this.commKeys.macKey = macKey;
        this.apiKey = apiKey;
    }

    public UserObjectInfoBase(long uoid, byte[] encKey, byte[] macKey, String apiKey, EBEndpointInfo endpointInfo) {
        this.uoid = uoid;
        this.commKeys.encKey = encKey;
        this.commKeys.macKey = macKey;
        this.apiKey = apiKey;
        this.endpointInfo = endpointInfo;
    }

    /**
     * Builds UserObjectInfoBase from serialized form.
     * @param encoded byte representation of the object
     * @return new object loaded from byte representation
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static UserObjectInfoBase build(byte[] encoded) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = null;
        ObjectInput in = null;
        try {
            bis = new ByteArrayInputStream(encoded);
            in = new ObjectInputStream(bis);
            return (UserObjectInfoBase) in.readObject();

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    /**
     * Serializes object to the byte array
     * @return encoded object representation.
     * @throws IOException
     */
    public byte[] getEncoded() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(this);
            return bos.toByteArray();

        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    @Override
    public String toString() {
        return "UserObjectInfoBase{" +
                "uoid=" + uoid +
                ", userObjectType=" + userObjectType +
                ", commKeys=" + commKeys +
                ", apiKey='" + apiKey + '\'' +
                ", endpointInfo=" + endpointInfo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserObjectInfoBase that = (UserObjectInfoBase) o;

        if (uoid != that.uoid) return false;
        if (userObjectType != that.userObjectType) return false;
        if (commKeys != null ? !commKeys.equals(that.commKeys) : that.commKeys != null) return false;
        if (apiKey != null ? !apiKey.equals(that.apiKey) : that.apiKey != null) return false;
        return endpointInfo != null ? endpointInfo.equals(that.endpointInfo) : that.endpointInfo == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (uoid ^ (uoid >>> 32));
        result = 31 * result + (int) (userObjectType ^ (userObjectType >>> 32));
        result = 31 * result + (commKeys != null ? commKeys.hashCode() : 0);
        result = 31 * result + (apiKey != null ? apiKey.hashCode() : 0);
        result = 31 * result + (endpointInfo != null ? endpointInfo.hashCode() : 0);
        return result;
    }

    public long getUoid() {
        return uoid;
    }

    public UserObjectInfoBase setUoid(long uoid) {
        this.uoid = uoid;
        return this;
    }

    public byte[] getEncKey() {
        return commKeys.encKey;
    }

    public UserObjectInfoBase setEncKey(byte[] encKey) {
        this.commKeys.encKey = encKey;
        return this;
    }

    public byte[] getMacKey() {
        return commKeys.macKey;
    }

    public UserObjectInfoBase setMacKey(byte[] macKey) {
        this.commKeys.macKey = macKey;
        return this;
    }

    public String getApiKey() {
        return apiKey;
    }

    public UserObjectInfoBase setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public EBCommKeys getCommKeys() {
        return commKeys;
    }

    public void setCommKeys(EBCommKeys commKeys) {
        this.commKeys = commKeys;
    }

    public long getUserObjectType() {
        return userObjectType;
    }

    public void setUserObjectType(long userObjectType) {
        this.userObjectType = userObjectType;
    }

    public EBEndpointInfo getEndpointInfo() {
        return endpointInfo;
    }

    public UserObjectInfoBase setEndpointInfo(EBEndpointInfo endpointInfo) {
        this.endpointInfo = endpointInfo;
        return this;
    }
}
