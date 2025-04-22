
import string
import random
from pathlib import Path
from subprocess import run

# ----------------------------------------------------------------
inFile = "C:/doc/videocut/WJUG211C.mp4"
inCutStart = '00:19:57.2'
inCutEnd = '00:59:15.6'
# ----------------------------------------------------------------
#
# See:
#   https://trac.ffmpeg.org/wiki/Concatenate
#   https://www.labnol.org/internet/useful-ffmpeg-commands/28490
#   https://stackoverflow.com/questions/54189099/ffmpeg-remove-2-sec-from-middle-of-video-and-concat-the-parts-single-line-solut
#
# ----------------------------------------------------------------


inFilePath = Path(inFile)
inFileAbsolute = inFilePath.absolute()
inFileParent = inFilePath.parent
inFileBaseName = inFilePath.stem
inFileExtension = inFilePath.suffix
if not inFilePath.is_file():
  print(f'\n\nError: input file does not exist:\n  {inFile}\n  {inFileAbsolute}')
  exit(1)

randomPart = ''.join(random.choice(string.ascii_lowercase + string.digits) for _ in range(6))

cutLeft = inFileParent.joinpath(f'{inFileBaseName}-LEFT-{randomPart}{inFileExtension}')
cutLeftFile = cutLeft.absolute()
cutRight = inFileParent.joinpath(f'{inFileBaseName}-RIGHT-{randomPart}{inFileExtension}')
cutRightFile = cutRight.absolute()
cutFileList = inFileParent.joinpath(f'{inFileBaseName}-LIST-{randomPart}.txt')
cutFileListFile = cutFileList.absolute()
cutConcatFile = inFileParent.joinpath(f'{inFileBaseName}-CONCAT-{randomPart}{inFileExtension}').absolute()

run(['ffmpeg', '-ss', '00:00:00.0', '-to', inCutStart, '-i', inFileAbsolute,  '-vcodec', 'copy', '-acodec', 'copy', cutLeftFile])
run(['ffmpeg', '-ss', inCutEnd, '-i', inFileAbsolute, '-vcodec', 'copy', '-acodec', 'copy',   cutRightFile])

with open(cutFileListFile, 'a+') as file:
  file.write(f"file '{cutLeftFile}'\n")
  file.write(f"file '{cutRightFile}'")
  file.close()

run(['ffmpeg', '-f', 'concat', '-safe', '0', '-i', cutFileListFile, '-vcodec', 'copy', '-acodec', 'copy', cutConcatFile])

cutLeft.unlink(True)
cutRight.unlink(True)
cutFileList.unlink(True)

