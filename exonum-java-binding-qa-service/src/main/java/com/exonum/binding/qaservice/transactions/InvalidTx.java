package com.exonum.binding.qaservice.transactions;

import static com.exonum.binding.qaservice.transactions.QaTransactionTemplate.newQaTransactionBuilder;
import static com.exonum.binding.qaservice.transactions.TransactionPreconditions.checkMessageSize;
import static com.exonum.binding.qaservice.transactions.TransactionPreconditions.checkTransaction;

import com.exonum.binding.messages.BinaryMessage;
import com.exonum.binding.messages.Message;
import com.exonum.binding.messages.Transaction;
import com.exonum.binding.qaservice.transactions.converters.TransactionMessageConverter;
import com.exonum.binding.storage.database.Fork;

/**
 * An invalid transaction always returning false in {@link #isValid()}.
 */
final class InvalidTx implements Transaction {

  private static final short ID = QaTransaction.INVALID.id;

  @Override
  public boolean isValid() {
    return false;
  }

  @Override
  public void execute(Fork view) {
    throw new AssertionError("Must never be executed by the framework: " + this);
  }

  @Override
  public BinaryMessage getMessage() {
    return converter().toMessage(this);
  }

  static TransactionMessageConverter<InvalidTx> converter() {
    return TransactionConverter.INSTANCE;
  }

  private enum TransactionConverter implements TransactionMessageConverter<InvalidTx> {
    INSTANCE;

    static final int BODY_SIZE = 0;

    @Override
    public InvalidTx fromMessage(Message txMessage) {
      checkMessage(txMessage);
      return new InvalidTx();
    }

    @Override
    public BinaryMessage toMessage(InvalidTx transaction) {
      return newQaTransactionBuilder(ID)
          .buildRaw();
    }

    private void checkMessage(Message txMessage) {
      checkTransaction(txMessage, ID);
      checkMessageSize(txMessage, BODY_SIZE);
    }
  }
}
