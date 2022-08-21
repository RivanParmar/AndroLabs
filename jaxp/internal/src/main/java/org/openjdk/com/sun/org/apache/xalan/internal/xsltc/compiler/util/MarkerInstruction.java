/*
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
/*
 * Copyright 2001-2004 The Apache Software Foundation.
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

/*
 * $Id: MethodGenerator.java,v 1.10 2010-11-01 04:34:19 joehw Exp $
 */
package org.openjdk.com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import org.openjdk.com.sun.org.apache.bcel.internal.Constants;
import org.openjdk.com.sun.org.apache.bcel.internal.generic.ClassGen;
import org.openjdk.com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import org.openjdk.com.sun.org.apache.bcel.internal.generic.Instruction;
import org.openjdk.com.sun.org.apache.bcel.internal.generic.InstructionList;
import org.openjdk.com.sun.org.apache.bcel.internal.generic.Visitor;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A special abstract dummy subclass of {@link org.apache.bcel.generic.Instruction} used to mark
 * locations of interest in an {@link InstructionList}. It and its subclasses are only used as
 * placeholders, and do not contribute to the actual byte code instruction stream.
 */
abstract class MarkerInstruction extends Instruction {
    /**
     * Zero-argument constructor. Sets the opcode to an invalid value and sets the length to zero,
     * as it will not be written as part of the generated byte code.
     */
    public MarkerInstruction() {
        super(Constants.UNDEFINED, (short) 0);
    }

    /**
     * {@link Visitor}s will know nothing about this kind of {@link
     * org.apche.bcel.generic.Instruction}, so this method does nothing.
     */
    public void accept(Visitor v) {}

    /**
     * The number of JVM stack entries consumed by the instruction. This instruction is just a place
     * holder, so it does not consume any stack entries.
     *
     * @param cpg The {@link ConstantPoolGen} for the current {@link ClassGen}
     * @return <code>0</code> always
     */
    public final int consumeStack(ConstantPoolGen cpg) {
        return 0;
    }
    /**
     * The number of JVM stack entries produced by the instruction. This instruction is just a place
     * holder, so it does not produce any stack entries.
     *
     * @param cpg The {@link ConstantPoolGen} for the current {@link ClassGen}
     * @return <code>0</code> always
     */
    public final int produceStack(ConstantPoolGen cpg) {
        return 0;
    }

    /**
     * Produce a copy of the instruction. By default a {@link MarkerInstruction} has no parameters,
     * so the base implementation of {@link #copy()} returns the instruction itself.
     *
     * @return The instruction itself.
     */
    public Instruction copy() {
        return this;
    }
    /**
     * Dump instruction as byte code to stream out. A {@link MarkerInstruction} has no effect on the
     * generated byte code so it is never emitted to the output stream.
     *
     * @param out Output stream
     */
    public final void dump(DataOutputStream out) throws IOException {}
}
