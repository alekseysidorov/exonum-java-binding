/*
 * Copyright 2018 The Exonum Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exonum.binding.cryptocurrency;

import com.exonum.binding.common.crypto.PublicKey;
import com.exonum.binding.core.service.Service;
import com.exonum.binding.core.transaction.ExecutionException;
import com.exonum.binding.core.transaction.TransactionContext;
import com.exonum.binding.cryptocurrency.transactions.TxMessageProtos;
import java.util.List;
import java.util.Optional;

public interface CryptocurrencyService extends Service {

  Optional<Wallet> getWallet(PublicKey ownerKey);

  List<HistoryEntity> getWalletHistory(PublicKey ownerKey);

  /**
   * Creates a new named wallet with the given initial balance.
   *
   * @throws ExecutionException if the wallet of the tx author already exists
   */
  void createWallet(TxMessageProtos.CreateWalletTx arguments, TransactionContext context);

  /**
   * Transfers tokens between two wallets.
   *
   * @throws ExecutionException if the sender or receiver are unknown; the sender
   *     has insufficient funds; or the sender attempts a transfer to itself
   */
  void transfer(TxMessageProtos.TransferTx arguments, TransactionContext context);
}
