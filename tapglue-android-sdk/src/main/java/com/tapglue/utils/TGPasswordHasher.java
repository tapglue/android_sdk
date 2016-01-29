/*
 * Copyright (c) 2015-2016 Tapglue (https://www.tapglue.com/). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tapglue.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class TGPasswordHasher {

    /**  */
    private final int mDerivedKeyLength;

    /**  */
    private final int mEncoderType;

    /**  */
    private final int mIterations;

    /**  */
    private final String mSecRandomAlgorithm = "SHA1PRNG";

    private final String salt = "Salt String";

    @Nullable
    public static String hashPassword(@NonNull String password) {
        try {
            return new TGPasswordHasher(1000, 160, TGPasswordHasher.EncoderType.HEX).hashPasswordNonStatic(password);
        } catch (@NonNull NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return password;
    }

    /**
     * Minimum rounds 1000 anything less will be set as 1000.<br> The higher the number the longer
     * the encryption will take.
     *
     * @param iterations The number of rounds to to use during the <tt>String</tt> encryption.
     */
    public TGPasswordHasher(int iterations) {
        this(iterations, 0x0A0);
    }

    /**
     * Minimum rounds 1000 anything less will be set as 1000.<br> The higher the number the longer
     * the encryption will take.
     *
     * @param iterations The number of rounds to to use during the <tt>String</tt> encryption.
     * @param keyLength  The length of the key generated to hash the password default is 160
     */
    private TGPasswordHasher(int iterations, int keyLength) {
        this(iterations, keyLength, EncoderType.WEB_SAFE_BASE64);
    }

    /**
     * Minimum rounds 1000 anything less will be set as 1000.<br> The higher the number the longer
     * the encryption will take.
     *
     * @param iterations  The number of rounds to to use during the <tt>String</tt> encryption.
     * @param keyLength   The length of the key generated to hash the password default is 160
     * @param encoderType Pick one of the values from {@link EncoderType} default is {@link
     *                    EncoderType#WEB_SAFE_BASE64}
     */
    private TGPasswordHasher(int iterations, int keyLength, int encoderType) {
        // Set the number of rounds preformed during the hashing
        /*  */
        int mMinIterations = 0x3E8;
        if (iterations >= mMinIterations) { this.mIterations = iterations; }
        else { this.mIterations = mMinIterations; }

        // Set the length of the key used during hashing
        if (keyLength < 160) { mDerivedKeyLength = 0x0A0; }
        else { mDerivedKeyLength = keyLength; }

        // This goes a long with the EncoderType interface
        switch (encoderType) {
            case EncoderType.HEX:
                this.mEncoderType = EncoderType.HEX;
                break;
            case EncoderType.WEB_SAFE_BASE64:
                this.mEncoderType = EncoderType.WEB_SAFE_BASE64;
                break;

            default:
                this.mEncoderType = EncoderType.WEB_SAFE_BASE64;
        }
    }

    /**
     * @param attemptedPassword
     * @param encryptedPassword
     * @param salt
     *
     * @return
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchProviderException
     */
    private boolean authenticate(@NonNull String attemptedPassword, byte[] encryptedPassword, byte[] salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        byte[] encryptedAttemptedPassword = encryptedPassword(attemptedPassword, salt);
        return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
    }

    /**
     * @param password
     * @param salt
     *
     * @return
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchProviderException
     */
    private byte[] encryptedPassword(@NonNull String password, byte[] salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, mIterations, mDerivedKeyLength);
        /*  */
        String mKeyFactoryAlgorithm = "PBKDF2WithHmacSHA1";
        SecretKeyFactory f = SecretKeyFactory.getInstance(mKeyFactoryAlgorithm, "BC");
        return f.generateSecret(spec).getEncoded();
    }

    /**
     * @param password
     * @param includeSalt
     *
     * @return
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchProviderException
     */
    private String hashPasswordNonStatic(@NonNull String password, boolean includeSalt)
        throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {

        byte[] data = encryptedPassword(password, salt.getBytes(Charset.forName("UTF8")));

        switch (mEncoderType) {
            case EncoderType.HEX:
                return ((includeSalt ? HexCoder.toHex(salt.getBytes(Charset.forName("UTF8"))) + ":" : "") + HexCoder.toHex(data)).toLowerCase();

            case EncoderType.WEB_SAFE_BASE64:
                return ((includeSalt ? Base64Coder.encodeWebSafe(salt.getBytes(Charset.forName("UTF8"))) + ":" : "") + Base64Coder.encodeWebSafe(data)).toLowerCase();

            default:
                return null;
        }
    }
    // TODO Make this a variable

    /**
     * @param password
     *
     * @return
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchProviderException
     */
    @Nullable
    private String hashPasswordNonStatic(@NonNull String password)
        throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        return hashPasswordNonStatic(password, false);
    }

    /**
     * @param attemptedPassword
     * @param hashedPassword
     *
     * @return
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws Base64DecodingException
     * @throws NoSuchProviderException
     */
    public boolean verifyPassword(@NonNull String attemptedPassword, @NonNull String hashedPassword) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, Base64DecodingException {
        switch (mEncoderType) {
            case EncoderType.HEX:
                return verifyPassword(attemptedPassword, hashedPassword, HexCoder.toByte(salt));
            case EncoderType.WEB_SAFE_BASE64:
                return verifyPassword(attemptedPassword, hashedPassword, Base64Coder.decodeWebSafe(salt));

            default:
                return false;
        }
    }

    /**
     * @param attemptedPassword
     * @param salt
     *
     * @return
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws Base64DecodingException
     * @throws NoSuchProviderException
     */
    private boolean verifyPassword(@NonNull String attemptedPassword, @NonNull String hashedPassword, byte[] salt) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, Base64DecodingException {

        switch (mEncoderType) {
            case EncoderType.HEX:
                return authenticate(attemptedPassword, HexCoder.toByte(hashedPassword), salt);
            case EncoderType.WEB_SAFE_BASE64:
                return authenticate(attemptedPassword, Base64Coder.decodeWebSafe(hashedPassword), salt);

            default:
                return false;
        }

    }

    /**
     * To get a web safe Base64 encoded <tt>String</tt> use {@link EncoderType#WEB_SAFE_BASE64}
     * value is 1.<br> To get a HEX encoded <tt>String</tt> use {@link EncoderType#HEX} value is 0
     */
    public interface EncoderType {
        /**
         * Value 0
         */
        int HEX = 0;

        /**
         * Value 1
         */
        int WEB_SAFE_BASE64 = 1;
    }

    /**
     * A web-safe Base64 encoding and decoding utility class. See RFC 3548
     *
     * @author steveweis@gmail.com (Steve Weis)
     */
    static class Base64Coder {
        /**
         * Mapping table from 6-bit nibbles to Base64 characters.
         */
        private static final char[] ALPHABET = {'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '-', '_'};

        /**
         * Mapping table from Base64 characters to 6-bit nibbles.
         */
        private static final byte[] DECODE = new byte[128];

        private static final char[] WHITESPACE = {'\t', '\n', '\r', ' ', '\f'};

        static {
            for (int i = 0; i < DECODE.length; i++) {
                DECODE[i] = -1;
            }

            for (char aWHITESPACE : WHITESPACE) {
                DECODE[aWHITESPACE] = -2;
            }

            for (int i = 0; i < ALPHABET.length; i++) {
                DECODE[ALPHABET[i]] = (byte) i;
            }
        }

        /**
         * Decodes a web-safe Base64 encoded string.
         *
         * @deprecated Use {@link #decodeWebSafe(String)} instead.
         */
        @NonNull
        @Deprecated
        public static byte[] decode(@NonNull String source) throws Base64DecodingException {
            return decodeWebSafe(source);
        }

        /**
         * Decodes a MIME Base64 encoded string.
         *
         * @param source The string to decode. May contain whitespace and optionally up to two
         *               padding '=' characters.
         *
         * @return a byte array representation of the encoded data.
         *
         * @throws Base64DecodingException if the source string contains an illegal character or is
         *                                 of an illegal length (1 mod 4).
         */

        @NonNull
        public static byte[] decodeMime(String source)
            throws Base64DecodingException {
            source = source.replace('+', '-');
            source = source.replace('/', '_');
            return decodeWebSafe(source);
        }

        /**
         * Decodes a web-safe Base64 encoded string
         *
         * @param source The string to decode. May contain whitespace and optionally up to two
         *               padding '=' characters.
         *
         * @return A byte array representation of the encoded data.
         *
         * @throws Base64DecodingException If the source string contains an illegal character or is
         *                                 of an illegal length (1 mod 4).
         */
        @NonNull
        public static byte[] decodeWebSafe(@NonNull String source) throws Base64DecodingException {
            char[] input = source.toCharArray();
            int inLen = input.length;
            // Trim up to two trailing '=' padding characters
            if (input[inLen - 1] == '=') {
                inLen--;
            }
            if (input[inLen - 1] == '=') {
                inLen--;
            }

            // Ignore whitespace
            int whiteSpaceChars = 0;
            for (char c : input) {
                if (isWhiteSpace(c)) {
                    whiteSpaceChars++;
                }
            }

            inLen -= whiteSpaceChars;
            int inputBlocks = inLen / 4;
            int remainder = inLen % 4;
            int outputLen = inputBlocks * 3;
            switch (remainder) {
                case 1:
                    throw new Base64DecodingException(String.format(
                        "Input source is of illegal length: %s", inLen));
                case 2:
                    outputLen += 1;
                    break;
                case 3:
                    outputLen += 2;
                    break;
            }
            byte[] out = new byte[outputLen];
            int buffer = 0;
            int buffCount = 0;
            int outPos = 0;
            for (int i = 0; i < inLen + whiteSpaceChars; i++) {
                if (!isWhiteSpace(input[i])) {
                    buffer = (buffer << 6) | getByte(input[i]);
                    buffCount++;
                }
                if (buffCount == 4) {
                    out[outPos++] = (byte) (buffer >> 16);
                    out[outPos++] = (byte) (buffer >> 8);
                    out[outPos++] = (byte) buffer;
                    buffer = 0;
                    buffCount = 0;
                }
            }
            switch (buffCount) {
                case 2:
                    out[outPos++] = (byte) (buffer >> 4);
                    break;
                case 3:
                    out[outPos++] = (byte) (buffer >> 10);
                    out[outPos++] = (byte) (buffer >> 2);
                    break;
            }
            return out;
        }

        /**
         * Encodes an arbitrary array of input as a web-safe Base64 string.
         *
         * @deprecated Use {@link #encodeWebSafe(byte[])} instead.
         */
        @NonNull
        @Deprecated
        public static String encode(@NonNull byte[] input) {
            return encodeWebSafe(input);
        }

        /**
         * Encodes an arbitrary array of input as a MIME Base64 string.
         *
         * @param input Input bytes to encode as a MIME Base64 String
         *
         * @return A MIME Base64 representation of the input. This string will not be padded with
         * '=' characters.
         */

        @NonNull
        public static String encodeMime(@NonNull byte[] input) {
            String result = encodeWebSafe(input);
            result = result.replace('-', '+');
            result = result.replace('_', '/');
            switch (result.length() % 4) {
                case 0:
                    return result;
                case 2:
                    return result + "==";
                case 3:
                    return result + "=";
                case 1:
                default:
                    throw new RuntimeException("Bug in Base64 encoder");
            }
        }

        /**
         * Encodes an arbitrary array of input as a web-safe Base64 string.
         *
         * @param input Input bytes to encode as a web-safe Base64 String
         *
         * @return A web-safe Base64 representation of the input. This string will not be padded
         * with '=' characters.
         */
        @NonNull
        public static String encodeWebSafe(@NonNull byte[] input) {
            int inputBlocks = input.length / 3;
            int remainder = input.length % 3;
            int outputLen = inputBlocks * 4;

            switch (remainder) {
                case 1:
                    outputLen += 2;
                    break;
                case 2:
                    outputLen += 3;
                    break;
            }

            char[] out = new char[outputLen];
            int outPos = 0;
            int inPos = 0;

            for (int i = 0; i < inputBlocks; i++) {
                int buffer = (0xFF & input[inPos++]) << 16
                    | (0xFF & input[inPos++]) << 8 | (0xFF & input[inPos++]);
                out[outPos++] = ALPHABET[(buffer >> 18) & 0x3F];
                out[outPos++] = ALPHABET[(buffer >> 12) & 0x3F];
                out[outPos++] = ALPHABET[(buffer >> 6) & 0x3F];
                out[outPos++] = ALPHABET[buffer & 0x3F];
            }

            if (remainder > 0) {
                int buffer = (0xFF & input[inPos++]) << 16;
                if (remainder == 2) {
                    buffer |= (0xFF & input[inPos++]) << 8;
                }
                out[outPos++] = ALPHABET[(buffer >> 18) & 0x3F];
                out[outPos++] = ALPHABET[(buffer >> 12) & 0x3F];
                if (remainder == 2) {
                    out[outPos++] = ALPHABET[(buffer >> 6) & 0x3F];
                }
            }
            return new String(out);
        }

        private static byte getByte(int i) throws Base64DecodingException {
            if (i < 0 || i > 127 || DECODE[i] == -1) {
                throw new Base64DecodingException(String.format(
                    "Illegal character in Base64 string: %s", i));
            }
            return DECODE[i];
        }

        private static boolean isWhiteSpace(int i) {
            return DECODE[i] == -2;
        }

        private Base64Coder() {
            // Don't new me.
        }
    }

    private static class Base64DecodingException extends Exception {
        public Base64DecodingException(String format) {

        }
    }

    static class HexCoder {

        /**
         * HEX characters
         */
        private final static String HEX = "0123456789ABCDEF";

        /**
         * Append the <code>byte</code> to the {@link StringBuffer}
         */
        private static void appendHex(@NonNull StringBuffer sb, byte b) {
            sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
        }

        /**
         * @param hex The HEX String to convert
         *
         * @return The String
         */
        @NonNull
        public static String fromHex(@NonNull String hex) {
            return new String(toByte(hex));
        }

        /**
         * @param hexString The HEX {@link String} to convert
         *
         * @return The <code>byte[]</code> of the {@link String}
         */
        @NonNull
        public static byte[] toByte(@NonNull String hexString) {
            int len = hexString.length() / 2;
            byte[] result = new byte[len];
            for (int i = 0; i < len; i++) {
                result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
            }
            return result;
        }

        /**
         * @param buf The <code>byte[]</code> to convert
         *
         * @return The {@link String} of the <code>byte[]</code>
         */
        public static String toHex(@Nullable byte[] buf) {
            if (buf == null) { return ""; }
            StringBuffer result = new StringBuffer(2 * buf.length);
            for (byte aBuf : buf) {
                appendHex(result, aBuf);
            }
            return result.toString();
        }

        /**
         * @param txt The {@link String} to convert to a HEX String
         *
         * @return The converted HEX String
         */
        public static String toHex(@NonNull String txt) {
            return toHex(txt.getBytes());
        }
    }
}
