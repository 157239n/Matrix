package org.kelvinho.matrix;

import javax.annotation.Nonnull;

class MatrixNotInvertibleException extends RuntimeException {
    MatrixNotInvertibleException(@Nonnull String reason) {
        super(reason);
    }
}
