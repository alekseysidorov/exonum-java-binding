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

package com.exonum.binding.common.crypto;

import com.exonum.binding.test.CiOnly;
import com.goterl.lazycode.lazysodium.utils.LibraryLoader.Mode;
import org.junit.jupiter.api.DisplayName;

@Forked
@CiOnly
@DisplayName("Test that the Ed25519 crypto function works with the libsodium, "
    + "bundled in lazysodium")
class Ed25519CryptoFunctionWithBundledTest extends Ed25519CryptoFunctionTestable {

  @Override
  Ed25519CryptoFunction createFunction() {
    return new Ed25519CryptoFunction(Mode.BUNDLED_ONLY);
  }
}
