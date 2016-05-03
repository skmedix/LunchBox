package org.apache.commons.codec.digest;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

public class DigestUtils {

    private static final int STREAM_BUFFER_LENGTH = 1024;

    private static byte[] digest(MessageDigest messagedigest, InputStream inputstream) throws IOException {
        return updateDigest(messagedigest, inputstream).digest();
    }

    public static MessageDigest getDigest(String s) {
        try {
            return MessageDigest.getInstance(s);
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            throw new IllegalArgumentException(nosuchalgorithmexception);
        }
    }

    public static MessageDigest getMd2Digest() {
        return getDigest("MD2");
    }

    public static MessageDigest getMd5Digest() {
        return getDigest("MD5");
    }

    public static MessageDigest getSha1Digest() {
        return getDigest("SHA-1");
    }

    public static MessageDigest getSha256Digest() {
        return getDigest("SHA-256");
    }

    public static MessageDigest getSha384Digest() {
        return getDigest("SHA-384");
    }

    public static MessageDigest getSha512Digest() {
        return getDigest("SHA-512");
    }

    /** @deprecated */
    @Deprecated
    public static MessageDigest getShaDigest() {
        return getSha1Digest();
    }

    public static byte[] md2(byte[] abyte) {
        return getMd2Digest().digest(abyte);
    }

    public static byte[] md2(InputStream inputstream) throws IOException {
        return digest(getMd2Digest(), inputstream);
    }

    public static byte[] md2(String s) {
        return md2(StringUtils.getBytesUtf8(s));
    }

    public static String md2Hex(byte[] abyte) {
        return Hex.encodeHexString(md2(abyte));
    }

    public static String md2Hex(InputStream inputstream) throws IOException {
        return Hex.encodeHexString(md2(inputstream));
    }

    public static String md2Hex(String s) {
        return Hex.encodeHexString(md2(s));
    }

    public static byte[] md5(byte[] abyte) {
        return getMd5Digest().digest(abyte);
    }

    public static byte[] md5(InputStream inputstream) throws IOException {
        return digest(getMd5Digest(), inputstream);
    }

    public static byte[] md5(String s) {
        return md5(StringUtils.getBytesUtf8(s));
    }

    public static String md5Hex(byte[] abyte) {
        return Hex.encodeHexString(md5(abyte));
    }

    public static String md5Hex(InputStream inputstream) throws IOException {
        return Hex.encodeHexString(md5(inputstream));
    }

    public static String md5Hex(String s) {
        return Hex.encodeHexString(md5(s));
    }

    /** @deprecated */
    @Deprecated
    public static byte[] sha(byte[] abyte) {
        return sha1(abyte);
    }

    /** @deprecated */
    @Deprecated
    public static byte[] sha(InputStream inputstream) throws IOException {
        return sha1(inputstream);
    }

    /** @deprecated */
    @Deprecated
    public static byte[] sha(String s) {
        return sha1(s);
    }

    public static byte[] sha1(byte[] abyte) {
        return getSha1Digest().digest(abyte);
    }

    public static byte[] sha1(InputStream inputstream) throws IOException {
        return digest(getSha1Digest(), inputstream);
    }

    public static byte[] sha1(String s) {
        return sha1(StringUtils.getBytesUtf8(s));
    }

    public static String sha1Hex(byte[] abyte) {
        return Hex.encodeHexString(sha1(abyte));
    }

    public static String sha1Hex(InputStream inputstream) throws IOException {
        return Hex.encodeHexString(sha1(inputstream));
    }

    public static String sha1Hex(String s) {
        return Hex.encodeHexString(sha1(s));
    }

    public static byte[] sha256(byte[] abyte) {
        return getSha256Digest().digest(abyte);
    }

    public static byte[] sha256(InputStream inputstream) throws IOException {
        return digest(getSha256Digest(), inputstream);
    }

    public static byte[] sha256(String s) {
        return sha256(StringUtils.getBytesUtf8(s));
    }

    public static String sha256Hex(byte[] abyte) {
        return Hex.encodeHexString(sha256(abyte));
    }

    public static String sha256Hex(InputStream inputstream) throws IOException {
        return Hex.encodeHexString(sha256(inputstream));
    }

    public static String sha256Hex(String s) {
        return Hex.encodeHexString(sha256(s));
    }

    public static byte[] sha384(byte[] abyte) {
        return getSha384Digest().digest(abyte);
    }

    public static byte[] sha384(InputStream inputstream) throws IOException {
        return digest(getSha384Digest(), inputstream);
    }

    public static byte[] sha384(String s) {
        return sha384(StringUtils.getBytesUtf8(s));
    }

    public static String sha384Hex(byte[] abyte) {
        return Hex.encodeHexString(sha384(abyte));
    }

    public static String sha384Hex(InputStream inputstream) throws IOException {
        return Hex.encodeHexString(sha384(inputstream));
    }

    public static String sha384Hex(String s) {
        return Hex.encodeHexString(sha384(s));
    }

    public static byte[] sha512(byte[] abyte) {
        return getSha512Digest().digest(abyte);
    }

    public static byte[] sha512(InputStream inputstream) throws IOException {
        return digest(getSha512Digest(), inputstream);
    }

    public static byte[] sha512(String s) {
        return sha512(StringUtils.getBytesUtf8(s));
    }

    public static String sha512Hex(byte[] abyte) {
        return Hex.encodeHexString(sha512(abyte));
    }

    public static String sha512Hex(InputStream inputstream) throws IOException {
        return Hex.encodeHexString(sha512(inputstream));
    }

    public static String sha512Hex(String s) {
        return Hex.encodeHexString(sha512(s));
    }

    /** @deprecated */
    @Deprecated
    public static String shaHex(byte[] abyte) {
        return sha1Hex(abyte);
    }

    /** @deprecated */
    @Deprecated
    public static String shaHex(InputStream inputstream) throws IOException {
        return sha1Hex(inputstream);
    }

    /** @deprecated */
    @Deprecated
    public static String shaHex(String s) {
        return sha1Hex(s);
    }

    public static MessageDigest updateDigest(MessageDigest messagedigest, byte[] abyte) {
        messagedigest.update(abyte);
        return messagedigest;
    }

    public static MessageDigest updateDigest(MessageDigest messagedigest, InputStream inputstream) throws IOException {
        byte[] abyte = new byte[1024];

        for (int i = inputstream.read(abyte, 0, 1024); i > -1; i = inputstream.read(abyte, 0, 1024)) {
            messagedigest.update(abyte, 0, i);
        }

        return messagedigest;
    }

    public static MessageDigest updateDigest(MessageDigest messagedigest, String s) {
        messagedigest.update(StringUtils.getBytesUtf8(s));
        return messagedigest;
    }
}
