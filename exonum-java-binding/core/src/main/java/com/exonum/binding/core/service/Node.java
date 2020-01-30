/*
 * Copyright 2019 The Exonum Team
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

package com.exonum.binding.core.service;

import com.exonum.binding.common.crypto.PublicKey;
import com.exonum.binding.common.hash.HashCode;
import com.exonum.binding.core.blockchain.Blockchain;
import com.exonum.binding.core.blockchain.BlockchainData;
import com.exonum.binding.core.storage.database.Prefixed;
import com.exonum.binding.core.transaction.RawTransaction;
import java.util.function.Function;

/**
 * An Exonum node context. Allows to add transactions to Exonum network
 * and get a snapshot of the database state.
 *
 * <p>Once the node is <em>closed</em>, it can no longer be used. Methods of a closed Node
 * will throw an {@link IllegalStateException} if invoked.
 *
 * <p>All method arguments are non-null by default.
 */
public interface Node extends AutoCloseable {

  /**
   * Creates a transaction from the given parameters, signs it with
   * the {@linkplain #getPublicKey() node service key}, and then submits it into Exonum network.
   * This node does <em>not</em> execute the transaction immediately, but broadcasts it to all
   * the nodes in the network. Then each node verifies the transaction and, if it is correct,
   * adds it to the <a href="https://exonum.com/doc/version/0.13-rc.2/advanced/consensus/specification/#pool-of-unconfirmed-transactions">pool of unconfirmed transactions</a>.
   * The transaction is executed later asynchronously.
   *
   * <p>Incorrect transactions (e.g., the payload of which cannot be deserialized by the target
   * service, or which have unknown message id) are rejected by the network.
   *
   * <p><em>Be aware that each node has its own service key pair, therefore
   * invocations of this method on different nodes will produce different transactions.</em>
   *
   * @param rawTransaction transaction parameters to include in transaction message
   * @return hash of the transaction message created by the framework
   * @throws TransactionSubmissionException if the transaction belongs to an unknown service,
   *     or cannot be submitted
   * @see Blockchain#getTxMessages()
   */
  HashCode submitTransaction(RawTransaction rawTransaction);

  /**
   * Performs the given function with a snapshot of the current database state.
   * Only the executing service data is accessible through the provided snapshot.
   *
   * <p>A shortcut for {@link BlockchainData#getExecutingServiceData()}.
   *
   * @param <ResultT> a type the function returns
   * @param snapshotFunction a function to execute
   * @return the result of applying the given function to the database state
   * @see #withSnapshot(Function)
   */
  default <ResultT> ResultT withServiceData(Function<? super Prefixed, ResultT> snapshotFunction) {
    return withSnapshot(snapshotFunction.compose(BlockchainData::getExecutingServiceData));
  }

  // todo: Shall we rename? The present name highlights it is "read-only".
  /**
   * Performs the given function with a snapshot of the current database state.
   *
   * @param <ResultT> a type the function returns
   * @param snapshotFunction a function to execute
   * @return the result of applying the given function to the database state
   * @see #withServiceData(Function)
   */
  <ResultT> ResultT withSnapshot(Function<BlockchainData, ResultT> snapshotFunction);

  /**
   * Returns the service public key of this node. The corresponding private key is used
   * for signing transactions in {@link #submitTransaction(RawTransaction)}.
   *
   * <p>This key is stored under "service_public_key" key in the node configuration file.
   */
  PublicKey getPublicKey();

  /**
   * Closes this node. A closed node must no longer be used.
   */
  @Override
  void close();
}
