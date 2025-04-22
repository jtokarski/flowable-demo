
from pathlib import Path
from subprocess import run

# ----------------------------------------------------------------
inFile = "C:/doc/videocut/WJUG211.mp4"
inCutStart = '00:05:05.0'
inCutEnd = None
# ----------------------------------------------------------------
#
# See:
#   https://loopduplicate.com/content/lossless-video-cut-with-ffmpeg
#   https://superuser.com/questions/258032/is-it-possible-to-use-ffmpeg-to-trim-off-x-seconds-from-the-beginning-of-a-video
#
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

cutFile = inFileParent.joinpath(f'{inFileBaseName}-CUT{inFileExtension}').absolute()

ffmpegCommand = ['ffmpeg']
ffmpegCommand += ['-ss', inCutStart]
if inCutEnd:
  ffmpegCommand += ['-to', inCutEnd]
ffmpegCommand += ['-i', inFileAbsolute, '-vcodec', 'copy', '-acodec', 'copy', cutFile]
run(ffmpegCommand)
