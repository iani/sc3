// plot2d by aucotsi Sat Jul 27 16:11:03 2013 based on plot3
+ GNUPlot {
	plot2d {|data, label="", title, style="lines"|
		var fh, tmpname; // = this.createTempFile3( data, ns );
		defer {
			tmpname = this.pr_tmpname;
			this.class.pr_writeTempData2(data, tmpname: tmpname);

			["GNUPlot.plot2d data size: ", data.size].postln;
			title !? {pipe.putString("set title %\n".format(title.asString.quote))};
			pipe.putString("plot % with % title %\n".format(tmpname.asString.quote, style, label.asString.quote));
			lastdata = [ data ];
			pipe.flush;
		}
	}
}
