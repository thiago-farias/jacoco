/*******************************************************************************
 * Copyright (c) 2009, 2021 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.cli.internal.commands;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jacoco.cli.internal.Command;
import org.jacoco.core.tools.ExecFileLoader;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

/**
 * The <code>diff</code> command.
 */
public class Diff extends Command {

	@Argument(usage = "list of JaCoCo *.exec files to read. The superset must come first.", metaVar = "<execfiles>")
	List<File> execfiles = new ArrayList<File>();

	@Option(name = "--destfile", usage = "file to write diff execution data to", metaVar = "<path>", required = true)
	File destfile;

	@Override
	public String description() {
		return "Creates a new exec file containing all data that exists in the superset and does not exist in the othar files.";
	}

	@Override
	public int execute(final PrintWriter out, final PrintWriter err)
			throws IOException {
		final ExecFileLoader loader = loadExecutionData(out);
		out.printf("[INFO] Writing execution data to %s.%n",
				destfile.getAbsolutePath());
		loader.save(destfile, true);
		return 0;
	}

	private ExecFileLoader loadExecutionData(final PrintWriter out)
			throws IOException {
		final ExecFileLoader loader = new ExecFileLoader(true);
		if (execfiles.isEmpty()) {
			out.println("[WARN] No execution data files provided.");
		} else {
			boolean first = true;
			for (final File file : execfiles) {
				out.printf("[INFO] Loading execution data file %s.%n",
						file.getAbsolutePath());
				loader.load(file, first);
				first = false;
			}
		}
		return loader;
	}

}
