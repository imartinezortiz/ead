/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2014 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          CL Profesor Jose Garcia Santesmases 9,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.eucm.ead.engine.expressions;

import es.eucm.ead.engine.variables.VarsContext;

/**
 * An expression node that contains a reference to a variable.
 * 
 * @author mfreire
 */
public class VariableRef extends Expression {

	/**
	 * Naming convention for REFERENCE (if no $ is found, a LITERAL is assumed)
	 */
	public final static String REF_PREFIX = "$";

	/**
	 * Name of the variable. Type is not stored (although it *is* cached after a
	 * lookup).
	 */
	private final String name;

	/**
	 * For use in graphical editors.
	 * 
	 * @return an undecorated (no prefixes, suffixes or quoting) string
	 *         representation
	 */
	public String toNakedString() {
		return "" + name;
	}

	public VariableRef(String variableName) {
		this.name = variableName;
		this.isConstant = false;
	}

	@Override
	protected StringBuilder buildString(StringBuilder sb,
			boolean updateTokenPositions) {
		if (updateTokenPositions) {
			tokenPosition = sb.length();
		}

		sb.append(REF_PREFIX);
		sb.append(name);
		return sb;
	}

	@Override
	public Object evaluate(VarsContext context, boolean lazy)
			throws ExpressionEvaluationException {
		if (!context.hasVariable(name)) {
			throw new ExpressionEvaluationException("No variable named '"
					+ name + "'", this);
		}
		return context.getValue(name);
	}
}
