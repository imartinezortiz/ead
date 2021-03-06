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
package es.eucm.ead.engine.expressions.operators;

import es.eucm.ead.engine.Accessor;
import es.eucm.ead.engine.expressions.ExpressionEvaluationException;
import es.eucm.ead.engine.expressions.Operation;
import es.eucm.ead.engine.variables.VarsContext;

/**
 * Created by Javier Torrente on 2/05/14.
 */
public class AccessProperty extends Operation {

	private Accessor accessor;

	public AccessProperty(Accessor accessor) {
		super(2, 2);
		this.accessor = accessor;
	}

	@Override
	public Object evaluate(VarsContext context, boolean lazy)
			throws ExpressionEvaluationException {

		Object first = first().evaluate(context, lazy);
		Object second = second().evaluate(context, lazy);

		if (!(second instanceof String)) {
			throw new ExpressionEvaluationException(
					"\"prop\" operator needs a string as second operator", this);
		}
		try {
			return accessor.get(first, (String) second);
		} catch (Accessor.AccessorException ae) {
			throw new ExpressionEvaluationException(
					"An error occurred evaluating \"prop\" operator", this, ae);
		}
	}
}
