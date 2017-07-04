package com.rssoftware.ou.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class OTPGenUtil 
{
	 public static final long T0 = 0;
	 public static final long X = 30;
//	 public static final String returnDigits = "6";
	 private static String returnDigits = "6";
	// public static final String returnDigits = "4";
	 public static final Long TIME_FACTOR = 1L;
	 private static final int[] DIGITS_POWER
	    // 0 1  2   3    4     5      6       7        8
	    = {1,10,100,1000,10000,100000,1000000,10000000,100000000 };

	 
	 private OTPGenUtil() 
	 {
		 
	 }

	    /**
	     * This method uses the JCE to provide the crypto algorithm.
	     * HMAC computes a Hashed Message Authentication Code with the
	     * crypto hash algorithm as a parameter.
	     *
	     * @param crypto: the crypto algorithm (HmacSHA1, HmacSHA256,
	     *                             HmacSHA512)
	     * @param keyBytes: the bytes to use for the HMAC key
	     * @param text: the message or text to be authenticated
	     */
	    private static byte[] hmac_sha(String crypto, byte[] keyBytes,byte[] text)
	    {
	        try 
	        {
	        		Mac hmac;
	        		hmac = Mac.getInstance(crypto);
	        		SecretKeySpec macKey =  new SecretKeySpec(keyBytes, "RAW");
	        		hmac.init(macKey);
	        		return hmac.doFinal(text);
	        } 
	        catch (GeneralSecurityException gse) 
	        {
	            	throw new UndeclaredThrowableException(gse);
	        }
	    }


	    /**
	     * This method converts a HEX string to Byte[]
	     *
	     * @param hex: the HEX string
	     *
	     * @return: a byte array
	     */

	    private static byte[] hexStr2Bytes(String hex)
	    {
	        // Adding one byte to get the right conversion
	        // Values starting with "0" can be converted
	    	byte[] bArray = new BigInteger("10" + hex,16).toByteArray();

	        // Copy all the REAL bytes, not the "first"
	        byte[] ret = new byte[bArray.length - 1];
	        for (int i = 0; i < ret.length; i++)
	            ret[i] = bArray[i+1];
	        return ret;
	    }

	    
	    /**
	     * This method generates a TOTP value for the given
	     * set of parameters.
	     *
	     * @param key: the shared secret, HEX encoded
	     * @param time: a value that reflects a time
	     * 
	     *
	     * @return: a numeric String in base 10 that includes
	     *              {@link truncationDigits} digits
	     */

	    public static String generateTOTP(String key,String time)
	    {
	        return generateTOTP(key, time, "HmacSHA1");
	    }


	    /**
	     * This method generates a TOTP value for the given
	     * set of parameters.
	     *
	     * @param key: the shared secret, HEX encoded
	     * @param time: a value that reflects a time
	     * 
	     *
	     * @return: a numeric String in base 10 that includes
	     *              {@link truncationDigits} digits
	     */

	    public static String generateTOTP256(String key,String time)
	    {
	    	returnDigits = "6";
	        return generateTOTP(key, time, "HmacSHA256");
	    }
	    /**
	     * This method generates a TOTP value for the given
	     * set of parameters.
	     *
	     * @param key: the shared secret, HEX encoded
	     * @param time: a value that reflects a time
	     * @param returnDigits: number of digits to return
	     *
	     * @return: a numeric String in base 10 that includes
	     *              {@link truncationDigits} digits
	     */

	    public static String generateTOTP512(String key,String time)
	    {
	    	returnDigits = "6";
	        return generateTOTP(key, time,"HmacSHA512");
	    }

	    public static String generateTOTP512(String key,String time,String digits)
	    {
	    	returnDigits = digits;
	        return generateTOTP(key, time,"HmacSHA512");
	    }

	    /**
	     * This method generates a TOTP value for the given
	     * set of parameters.
	     *
	     * @param key: the shared secret, HEX encoded
	     * @param time: a value that reflects a time
	     * @param returnDigits: number of digits to return
	     * @param crypto: the crypto function to use
	     *
	     * @return: a numeric String in base 10 that includes
	     *              {@link truncationDigits} digits
	     */

	    public static String generateTOTP(String key,String time,String crypto)
	    {
	        int codeDigits = Integer.decode(returnDigits).intValue();
	        String result = null;

	        // Using the counter
	        // First 8 bytes are for the movingFactor
	        // Compliant with base RFC 4226 (HOTP)
	        while (time.length() < 16 )
	            time = "0" + time;

	        // Get the HEX in a Byte[]
	        byte[] msg = hexStr2Bytes(time);
	        byte[] k = hexStr2Bytes(key);



	        byte[] hash = hmac_sha(crypto, k, msg);

	        // put selected bytes into result int
	        int offset = hash[hash.length - 1] & 0xf;

	        int binary =
	            ((hash[offset] & 0x7f) << 24) |
	            ((hash[offset + 1] & 0xff) << 16) |
	            ((hash[offset + 2] & 0xff) << 8) |
	            (hash[offset + 3] & 0xff);

	        int otp = binary % DIGITS_POWER[codeDigits];

	        result = Integer.toString(otp);
	        while (result.length() < codeDigits) {
	            result = "0" + result;
	        }
	        return result;
	    }
/** Generate seeds dynamically depending upon the digits passed	
 *    
 * @param digCount
 * @return seed as string for specified digits
 */

	    public static String getRandomSeed(int digCount)
	    {
	    	Random rnd = new Random();
	        final char[] ch = new char[digCount];
	        for(int i = 0; i < digCount; i++)
	        {
	            ch[i] = (char) ('0' + (i == 0 ? rnd.nextInt(9) + 1 : rnd.nextInt(10)));
	        }
	        return new String(ch);
	    }

	    public static int  getRandomNumber(int low,int high)
	    {
	    	Random r = new Random();
	    	return  r.nextInt(high-low) + low;
           
	    }
        

	    public static long getOTPTime()
	    {
	    	return System.currentTimeMillis();
	    }
	    
	    
	    public static String getSteps(long tm)
	    {
	    	String steps = "0";
	    	long T = tm/TIME_FACTOR;
	    	long interval = (T - T0)/X;
	    	steps = Long.toHexString(interval).toUpperCase();
	    	while (steps.length() < 16) steps = "0" + steps;
	    	return steps ;
	    }
// Generate OTP from seed & TS	    
	    public static String generateOtp(String key,String time ) throws NumberFormatException,SecurityException
	    {
	    	String genOtp = null;
	    	try
	    	{
	    		genOtp= generateTOTP512(key ,time);
	    	}
	    	catch (Exception e)
	    	{
	    		throw e;
	    	}
	    	return genOtp;
	    	
	    }
	    
	    

}
