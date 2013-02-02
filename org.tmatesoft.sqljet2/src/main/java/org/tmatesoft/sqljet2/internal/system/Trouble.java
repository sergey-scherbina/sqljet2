package org.tmatesoft.sqljet2.internal.system;

import java.io.IOException;

public class Trouble extends Exception {

	public enum Code {
		/** SQL error or missing database */
		ERROR,

		/** Internal logic error in SQLite */
		INTERNAL,

		/** Internal logic error in SQLite */
		PERM,

		/** Callback routine requested an abort */
		ABORT,

		/** The database file is locked */
		BUSY,

		/** A table in the database is locked */
		LOCKED,

		/** A malloc() failed */
		NOMEM,

		/** Attempt to write a readonly database */
		READONLY,

		/** Operation terminated by sqlite3_interrupt() */
		INTERRUPT,

		/** Some kind of disk I/O error occurred */
		IOERR,

		/** The database disk image is malformed */
		CORRUPT,

		/** NOT USED. Table or record not found */
		NOTFOUND,

		/** Insertion failed because database is full */
		FULL,

		/** Unable to open the database file */
		CANTOPEN,

		/** NOT USED. Database lock protocol error */
		PROTOCOL,

		/** Database is empty */
		EMPTY,

		/** The database schema changed */
		SCHEMA,

		/** String or BLOB exceeds size limit */
		TOOBIG,

		/** Abort due to constraint violation */
		CONSTRAINT,

		/** Data type mismatch */
		MISMATCH,

		/** Library used incorrectly */
		MISUSE,

		/** Uses OS features not supported on host */
		NOLFS,

		/** Authorization denied */
		AUTH,

		/** Auxiliary database format error */
		FORMAT,

		/** 2nd parameter to sqlite3_bind out of range */
		RANGE,

		/** File opened that is not a database file */
		NOTADB,

		DONE,

		/** Bad parameter value in function call wich impossible to execute */
		BAD_PARAMETER
	}

	private Code code;

	public Code getCode() {
		return code;
	}

	public Trouble(final Code code, final String message, final Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public Trouble(final Code code, final Throwable cause) {
		super(cause);
		this.code = code;
	}

	public Trouble(final Code code, final String message) {
		super(message);
		this.code = code;
	}

	public Trouble(final Code code) {
		this.code = code;
	}

	public Trouble(final IOException cause) {
		this(Code.IOERR, cause);
	}

}
