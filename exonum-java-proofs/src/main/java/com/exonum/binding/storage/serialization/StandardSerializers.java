package com.exonum.binding.storage.serialization;

import com.exonum.binding.hash.HashCode;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

/**
 * A collection of pre-defined serializers.
 */
// todo: test these guys with the code from exonum-serialization, when we migrate to JUnit5
// (ECR-642)
public final class StandardSerializers {

  /** Returns a serializer of strings in UTF-8. */
  public static Serializer<String> string() {
    return StringSerializer.INSTANCE;
  }

  /** Returns a serializer of hash codes. */
  public static Serializer<HashCode> hash() {
    return HashCodeSerializer.INSTANCE;
  }

  enum StringSerializer implements Serializer<String> {
    INSTANCE;

    @Override
    public byte[] toBytes(String value) {
      return value.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String fromBytes(byte[] serializedValue) {
      try {
        // Since the String(bytes, charset) constructor is specified so that
        // it "… always replaces malformed-input and unmappable-character sequences …",
        // it is not suitable for our use-case: we must reject malformed input.

        // Create a new decoder
        CharsetDecoder decoder = StandardCharsets.UTF_8
            .newDecoder()
            // Reject (= report as exception) malformed input.
            .onMalformedInput(CodingErrorAction.REPORT)
            // In case some valid UTF-8 characters are not encodable in UTF-16,
            // we replace them with the default replacement character.
            .onUnmappableCharacter(CodingErrorAction.REPLACE);

        // Decode the buffer in a character buffer
        CharBuffer strBuffer = decoder.decode(ByteBuffer.wrap(serializedValue));
        return new String(strBuffer.array(), strBuffer.arrayOffset(), strBuffer.remaining());
      } catch (CharacterCodingException e) {
        throw new IllegalArgumentException("Cannot decode the input", e);
      }
    }
  }

  enum HashCodeSerializer implements Serializer<HashCode> {
    INSTANCE;

    @Override
    public byte[] toBytes(HashCode value) {
      return value.asBytes();
    }

    @Override
    public HashCode fromBytes(byte[] serializedValue) {
      return HashCode.fromBytes(serializedValue);
    }
  }

  private StandardSerializers() {}
}
